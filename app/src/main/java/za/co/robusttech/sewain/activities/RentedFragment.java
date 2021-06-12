package za.co.robusttech.sewain.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.constants.IProductConstants;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.models.Rent;
import za.co.robusttech.sewain.utils.NavUtil;


public class RentedFragment extends Fragment {

    private List<Product> mProducts;
    public static final String CHECKOUT_PRODUCTS = "productList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_rented, container, false);

        TextView productName = view.findViewById(R.id.rent_product_name);
        TextView productBuyedOn = view.findViewById(R.id.rent_product_buyed_on);
        TextView productRentPrice = view.findViewById(R.id.rent_price);
        Button productRentPay = view.findViewById(R.id.rent_product_pay);

        mProducts = new ArrayList<>();

        DatabaseReference rentRef = FirebaseDatabase.getInstance().getReference("Rented");

        rentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FirebaseUser auth;
                    auth = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = auth.getUid();

                    String rentID = dataSnapshot.getKey();

                    FirebaseDatabase.getInstance().getReference("Products").child(rentID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Product product = snapshot.getValue(Product.class);
                                    mProducts.add(product);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                    DatabaseReference rentIdRef = FirebaseDatabase.getInstance().getReference("Rented").child(rentID).child(userId);

                    rentIdRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Rent rent = snapshot.getValue(Rent.class);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String currentDate = simpleDateFormat.format(new Date());

                            if (!snapshot.exists()){

                            }else{

                                productName.setText(rent.getProductName());
                                productBuyedOn.setText(rent.getFirstRentTime());
                                productRentPrice.setText(rent.getPerRent());

                                productRentPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (!snapshot.exists()){

                                        }else {
                                            if (rent.getSecondRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getThirdRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getFourthRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getFifthRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getSixthRentTime().equals(currentDate)) {

                                                showDialogRent();


                                            } else if (rent.getSeventhRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getEighthRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getNinthRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            } else if (rent.getTenthRentTime().equals(currentDate)) {

                                                showDialogRent();

                                            }else if (rent.getTenthRentTime().equals("payed")) {

                                                paymentComplete();

                                            } else{

                                                Toast.makeText(getActivity(), "You have time to pay Rent", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                    }
                                });

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


        return view;
    }

    private void showDialogRent() {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.checkout_payment_options, null);
        paymentOptions.findViewById(R.id.card_payment).setOnClickListener(this::cardPaymentRent);
        paymentOptions.findViewById(R.id.paypal_payment).setOnClickListener(this::paypayPaymentRent);
        paymentOptions.findViewById(R.id.cod_payment).setOnClickListener(this::codPaymentBuy);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Payment method to rent");
        builder.setView(paymentOptions);

        builder
                .setCancelable(true)
                .create()
                .show();
    }

    private void cardPaymentRent(View view) { Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(getActivity(), CheckoutActivityRentPayJava.class, bundle);

    }

    private void paypayPaymentRent(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(getActivity(), PayPalRentPayActivity.class, bundle);

    }

    private void codPaymentBuy(View view) {
        Toast.makeText(getActivity(), "Not Available", Toast.LENGTH_SHORT).show();

    }

    private void paymentComplete() {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.buy_or_return, null);
        paymentOptions.findViewById(R.id.buy_85_off).setOnClickListener(this::showDialogBuy);
        paymentOptions.findViewById(R.id.return_product).setOnClickListener(this::returnProduct);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What would you like to do");
        builder.setView(paymentOptions);

        builder
                .setCancelable(true)
                .create()
                .show();
    }

    private void showDialogBuy(View view) {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.checkout_payment_options, null);
        paymentOptions.findViewById(R.id.card_payment).setOnClickListener(this::buyProductCard);
        paymentOptions.findViewById(R.id.paypal_payment).setOnClickListener(this::buyProductPaypal);
        paymentOptions.findViewById(R.id.cod_payment).setOnClickListener(this::codPaymentBuy);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Method to buy product");
        builder.setView(paymentOptions);

        builder
                .setCancelable(true)
                .create()
                .show();

    }

    private void buyProductCard(View view) { Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(getActivity(), cardBuyActivity.class, bundle);

    }

    private void returnProduct(View view) {


        DatabaseReference rentRef = FirebaseDatabase.getInstance().getReference("Rented");

        rentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FirebaseUser auth;
                    auth = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = auth.getUid();

                    String rentID = dataSnapshot.getKey();

                    rentRef.child(rentID).child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Rent rent = snapshot.getValue(Rent.class);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Returned");
                            String productId = ref.push().getKey();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("UserId", userId);
                            map.put("ProductId", rent.getProductId());
                            map.put("RentedOn", rent.getFirstRentTime());

                            ref.child(productId).setValue(map);

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

        }

    private void buyProductPaypal(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(getActivity(), paypalBuyActivity.class, bundle);

    }

}
