/**
 * Project Name - sewa-in
 * Created on 2021/03/07 at 8:12 PM
 */

package za.co.robusttech.sewa_in.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Product;
import za.co.robusttech.sewa_in.utils.NavUtil;
import za.co.robusttech.sewa_in.utils.ProgressBar;

public class CheckoutActivityJava extends AppCompatActivity {

    private static final String TAG = "CheckoutActivityJava";
    // 10.0.2.2 is the Android emulator's alias to localhost
    //private static final String BACKEND_URL = "http://10.0.2.2:5000/";
    private static final String BACKEND_URL = "https://peaceful-sands-12649.herokuapp.com/";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private List<Product> checkoutProducts;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button paymentContinue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        // pk_test_TYooMQauvdEDq54NiTphI7jx

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);


        paymentContinue = findViewById(R.id.payment_continue);

        paymentContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(selectedId);

                if (radioButton.getText() != null){

                    if (radioButton.getText().toString().equals("Google Pay")){
                        Intent googlePay =  new Intent(CheckoutActivityJava.this , GooglePayActivity.class);
                        startActivity(googlePay);
                    }
                    if (radioButton.getText().toString().equals("PayPal")){
                        Intent paypal =  new Intent(CheckoutActivityJava.this , GooglePayActivity.class);
                        startActivity(paypal);
                    }
                }
            }
        });


        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(getString(R.string.stripe_publishable_key))
        );

        Bundle bundle = getIntent().getExtras();



        checkoutProducts = (List<Product>) bundle.getSerializable(AddCartActivity.CHECKOUT_PRODUCTS);

        System.out.println(checkoutProducts.toString());
        startCheckout();
    }

    private void startCheckout() {
        final String currencyCode = Objects.requireNonNull(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency()).getCurrencyCode();
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        double amount = getAmount() * 100;

        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();

        List<Map<String, Object>> itemList = new ArrayList<>();//getItemsList();

        payMap.put("currency", currencyCode);
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);

        String json = new Gson().toJson(payMap);
        Log.d(TAG, "startCheckout: json: " + json);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);

        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ProgressBar.displayDialog(this, "Processing payment...");
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }

        });
    }

    private double getAmount() {
        double amount = 0.0;
        for (Product product : checkoutProducts) {
            amount += product.getProductPrice();
        }

        return amount;
    }

    private List<Map<String, Object>> getItemsList() {
        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (Product product : checkoutProducts) {
            itemsList.add(product.toMap());
        }

        System.out.println("getItemsList: " + itemsList.toString());
        return itemsList;
    }

    // display alert on payment failed / successful
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);

        builder.setPositiveButton("Ok", ((dialog, which) ->  gotoHome()));
        builder.create().show();

        Log.d(TAG, "displayAlert: title: " + title + ". message: " + message);
        ProgressBar.hideDialog();

    }

    private void gotoHome() {
        NavUtil.moveTo(this, HomeActivity.class, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    // payment success
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );

        Log.d(TAG, "onPaymentSuccess: " + responseMap.toString());

        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    // callback request
    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
            Log.d(TAG, "onFailure: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
                Log.d(TAG, "onResponse: " + response.toString());
            } else {
                activity.onPaymentSuccess(response);

            }

        }
    }

    // payment result callback
    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityJava> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed", paymentIntent.getStatus().getCode()

                );

                removeCardItem();

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        private void removeCardItem() {
            FirebaseDatabase
                    .getInstance()
                    .getReference("Cart")
                    .child(getUserId())
                    .removeValue();
        }

        private String getUserId() {
            return Objects.requireNonNull(FirebaseAuth
                    .getInstance()
                    .getCurrentUser())
                    .getUid();
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}

