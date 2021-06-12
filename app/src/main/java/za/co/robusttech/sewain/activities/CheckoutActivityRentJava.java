/**
 * Project Name - sewa-in
 * Created on 2021/03/07 at 8:12 PM
 */

package za.co.robusttech.sewain.activities;


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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import za.co.robusttech.sewain.BuildConfig;
import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.utils.NavUtil;
import za.co.robusttech.sewain.utils.ProgressBar;

public class CheckoutActivityRentJava extends AppCompatActivity {

    private static final String TAG = "CheckoutActivityJava";
    // 10.0.2.2 is the Android emulator's alias to localhost
    //private static final String BACKEND_URL = "http://10.0.2.2:5000/";
    private static final String BACKEND_URL = BuildConfig.STRIPE_PAYMENT_SERVER_URL;

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


        for (Product product : checkoutProducts) {

           FirebaseDatabase.getInstance().getReference("Products").child(product.getProductId()).
                   addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                           Product priceProduct = snapshot.getValue(Product.class);

                           double amountPrice = priceProduct.getProductPrice()/10 * 10;

                           Map<String, Object> payMap = new HashMap<>();
                           Map<String, Object> itemMap = new HashMap<>();

                           Toast.makeText(CheckoutActivityRentJava.this, String.valueOf(amountPrice), Toast.LENGTH_SHORT).show();

                           List<Map<String, Object>> itemList = new ArrayList<>();//getItemsList();

                           payMap.put("currency", currencyCode);
                           itemMap.put("amount", amountPrice);
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
                                   .enqueue(new PayCallback(CheckoutActivityRentJava.this));

                           // Hook up the pay button to the card widget and stripe instance
                           Button payButton = findViewById(R.id.payButton);

                           payButton.setOnClickListener((View view) -> {
                               CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                               PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                               if (params != null) {
                                   ProgressBar.displayDialog(CheckoutActivityRentJava.this, "Processing payment...");
                                   ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                           .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                                   stripe.confirmPayment(CheckoutActivityRentJava.this, confirmParams);
                               }

                           });
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
        }

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
    private final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutActivityRentJava> activityRef;

        PayCallback(@NonNull CheckoutActivityRentJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityRentJava activity = activityRef.get();
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
            final CheckoutActivityRentJava activity = activityRef.get();
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
    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivityRentJava> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityRentJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityRentJava activity = activityRef.get();

            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert("Payment completed", paymentIntent.getStatus().getCode());

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = firebaseUser.getUid();

                    FirebaseDatabase
                            .getInstance()
                            .getReference("Cart")
                            .child(userId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) return;

                                    for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                                        Cart cart = currentSnapshot.getValue(Cart.class);
                                        assert cart != null;
                                        Product product = cart.getProduct();


                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                                        String firstTimeOutput = simpleDateFormat1.format(new Date());
                                        String date1 = firstTimeOutput;
                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                                        Calendar calendar12 = Calendar.getInstance();
                                        try {
                                            calendar12.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar12.add(Calendar.DATE, 7);//140
                                        SimpleDateFormat simpleDateFormat32 = new SimpleDateFormat("dd-MM-yyyy");
                                        String secondTimeOutput = simpleDateFormat32.format(calendar12.getTime());

                                        Calendar calendar13 = Calendar.getInstance();
                                        try {
                                            calendar13.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar13.add(Calendar.DATE, 14);//210
                                        SimpleDateFormat simpleDateFormat33 = new SimpleDateFormat("dd-MM-yyyy");
                                        String thirdTimeOutput = simpleDateFormat33.format(calendar13.getTime());

                                        Calendar calendar14 = Calendar.getInstance();
                                        try {
                                            calendar14.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar14.add(Calendar.DATE, 21);//280
                                        SimpleDateFormat simpleDateFormat34 = new SimpleDateFormat("dd-MM-yyyy");
                                        String fourthTimeOutput = simpleDateFormat34.format(calendar14.getTime());

                                        Calendar calendar15 = Calendar.getInstance();
                                        try {
                                            calendar15.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar15.add(Calendar.DATE, 28);//350
                                        SimpleDateFormat simpleDateFormat35 = new SimpleDateFormat("dd-MM-yyyy");
                                        String fivethTimeOutput = simpleDateFormat35.format(calendar15.getTime());

                                        Calendar calendar16 = Calendar.getInstance();
                                        try {
                                            calendar16.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar16.add(Calendar.DATE, 35);//420
                                        SimpleDateFormat simpleDateFormat36 = new SimpleDateFormat("dd-MM-yyyy");
                                        String sixthTimeOutput = simpleDateFormat36.format(calendar16.getTime());

                                        Calendar calendar17 = Calendar.getInstance();
                                        try {
                                            calendar17.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar17.add(Calendar.DATE, 42);//490
                                        SimpleDateFormat simpleDateFormat37 = new SimpleDateFormat("dd-MM-yyyy");
                                        String seventhTimeOutput = simpleDateFormat37.format(calendar17.getTime());

                                        Calendar calendar18 = Calendar.getInstance();
                                        try {
                                            calendar18.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar18.add(Calendar.DATE, 49);//560
                                        SimpleDateFormat simpleDateFormat38 = new SimpleDateFormat("dd-MM-yyyy");
                                        String eighthTimeOutput = simpleDateFormat38.format(calendar18.getTime());

                                        Calendar calendar19 = Calendar.getInstance();
                                        try {
                                            calendar19.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar19.add(Calendar.DATE, 56);//630
                                        SimpleDateFormat simpleDateFormat39 = new SimpleDateFormat("dd-MM-yyyy");
                                        String ninthTimeOutput = simpleDateFormat39.format(calendar19.getTime());

                                        Calendar calendar20 = Calendar.getInstance();
                                        try {
                                            calendar20.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar20.add(Calendar.DATE, 63);//700
                                        SimpleDateFormat simpleDateFormat40 = new SimpleDateFormat("dd-MM-yyyy");
                                        String tenthTimeOutput = simpleDateFormat40.format(calendar20.getTime());

                                        Calendar calendar21 = Calendar.getInstance();
                                        try {
                                            calendar21.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar21.add(Calendar.DATE, 65);//700
                                        SimpleDateFormat simpleDateFormat41 = new SimpleDateFormat("dd-MM-yyyy");
                                        String buyOrReturnTimeOutExpire = simpleDateFormat41.format(calendar21.getTime());


                                        Calendar calendar22 = Calendar.getInstance();
                                        try {
                                            calendar22.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar22.add(Calendar.DATE, 63);//700
                                        SimpleDateFormat simpleDateFormat42 = new SimpleDateFormat("dd-MM-yyyy");
                                        String buyOrReturnTimeOutOne = simpleDateFormat42.format(calendar22.getTime());

                                        Calendar calendar23 = Calendar.getInstance();
                                        try {
                                            calendar23.setTime(simpleDateFormat2.parse(date1));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar23.add(Calendar.DATE, 64);//700
                                        SimpleDateFormat simpleDateFormat43 = new SimpleDateFormat("dd-MM-yyyy");
                                        String buyOrReturnTimeOutTwo = simpleDateFormat43.format(calendar23.getTime());



                                        DatabaseReference priceRef = FirebaseDatabase.getInstance().getReference("Products").child(product.getProductId());

                                        priceRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                DatabaseReference buyRef = FirebaseDatabase.getInstance().getReference("Rented").child(product.getProductId()).child(userId);

                                                Product productRate = snapshot.getValue(Product.class);

                                                double currentProductPrice = productRate.getProductPrice();
                                                String productPrice = String.valueOf(currentProductPrice);

                                                double perRent = productRate.getProductPrice()/10;
                                                String amountPerRent = String.valueOf(perRent);

                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("id", userId);
                                                hashMap.put("productId", product.getProductId());
                                                hashMap.put("productPrice", productPrice);//totalPrice
                                                hashMap.put("productName", product.getProductName());

                                                hashMap.put("perRent", amountPerRent);//1time
                                                hashMap.put("buyOrReturnTimeExpire", buyOrReturnTimeOutExpire);
                                                hashMap.put("buyOrReturnTimeOutOne", buyOrReturnTimeOutOne);
                                                hashMap.put("buyOrReturnTimeOutTwo", buyOrReturnTimeOutTwo);

                                                hashMap.put("firstRentTime", firstTimeOutput);//70-today
                                                hashMap.put("secondRentTime", secondTimeOutput);//140
                                                hashMap.put("thirdRentTime", thirdTimeOutput);//210
                                                hashMap.put("fourthRentTime", fourthTimeOutput);//280
                                                hashMap.put("fifthRentTime", fivethTimeOutput);//350
                                                hashMap.put("sixthRentTime", sixthTimeOutput);//420
                                                hashMap.put("seventhRentTime", seventhTimeOutput);//490
                                                hashMap.put("eighthRentTime", eighthTimeOutput);//560
                                                hashMap.put("ninthRentTime", ninthTimeOutput);//630
                                                hashMap.put("tenthRentTime", tenthTimeOutput);//700-lastday

                                                buyRef.setValue(hashMap);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

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
            final CheckoutActivityRentJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}

