package za.co.robusttech.sewain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import za.co.robusttech.sewain.BuildConfig;
import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.models.Rent;
import za.co.robusttech.sewain.utils.NavUtil;

public class PayPalRentPayActivity extends AppCompatActivity {

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    private List<Product> checkoutProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        Bundle bundle = getIntent().getExtras();
        checkoutProducts = (List<Product>) bundle.getSerializable(AddCartActivity.CHECKOUT_PRODUCTS);
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        processPayment();
    }


//    public void onBuyPressed(View v) {
//        processPayment();
//    }

    private double getAmount() {
        double amount = 0.0;
        for (Product product : checkoutProducts) {
            amount += product.getProductPrice()/10;
        }

        return amount;
    }

    private void processPayment() {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(getAmount()), "USD", "Total",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    Toast.makeText(this, "Order was paid successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = firebaseUser.getUid();

                    DatabaseReference rentPay = FirebaseDatabase.getInstance().getReference("Rented");

                    rentPay.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {

                                String key = currentSnapshot.getKey();
                                rentPay.child(key).child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        Rent rent = snapshot.getValue(Rent.class);

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String currentDate = simpleDateFormat.format(new Date());

                                        if (!snapshot.exists()){

                                        }else{

                                            if (rent.getSecondRentTime().equals(currentDate)){

                                                rentPay.child(key).child(userId).child("secondRentTime").setValue("payed");


                                            }else if (rent.getThirdRentTime().equals(currentDate)){

                                                rentPay.child(key).child(userId).child("thirdRentTime").setValue("payed");

                                            }else if (rent.getFourthRentTime().equals(currentDate)){

                                                rentPay.child(key).child(userId).child("fourthRentTime").setValue("payed");


                                            }else if (rent.getFifthRentTime().equals(currentDate)){


                                                rentPay.child(key).child(userId).child("fifthRentTime").setValue("payed");


                                            }else if (rent.getSixthRentTime().equals(currentDate)){



                                                rentPay.child(key).child(userId).child("sixthRentTime").setValue("payed");


                                            }else if (rent.getSeventhRentTime().equals(currentDate)){


                                                rentPay.child(key).child(userId).child("seventhRentTime").setValue("payed");


                                            }else if (rent.getEighthRentTime().equals(currentDate)){


                                                rentPay.child(key).child(userId).child("eighthRentTime").setValue("payed");


                                            }else if (rent.getNinthRentTime().equals(currentDate)){


                                                rentPay.child(key).child(userId).child("ninthRentTime").setValue("payed");

                                            }else if (rent.getTenthRentTime().equals(currentDate)){


                                                rentPay.child(key).child(userId).child("tenthRentTime").setValue("payed");

                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    NavUtil.moveTo(this, HomeActivity.class, null);

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}