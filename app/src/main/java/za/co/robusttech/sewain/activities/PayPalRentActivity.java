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
import java.util.Objects;

import za.co.robusttech.sewain.BuildConfig;
import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.utils.NavUtil;

public class PayPalRentActivity extends AppCompatActivity {

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

    private void processPayment() {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        for (Product product : checkoutProducts) {

            DatabaseReference priceRef = FirebaseDatabase.getInstance().getReference("Products");
            priceRef.child(product.getProductId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Product priceProduct = snapshot.getValue(Product.class);


                    PayPalPayment payment = new PayPalPayment(new BigDecimal(priceProduct.getProductPrice()/10), "USD", "Total",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(PayPalRentActivity.this, PaymentActivity.class);

                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                    startActivityForResult(intent, 0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


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
                                    Toast.makeText(PayPalRentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                    removeCardItem();

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
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}