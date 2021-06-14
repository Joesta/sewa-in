package za.co.robusttech.sewain.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.adapter.Cart_WishListAdapter;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.models.address;
import za.co.robusttech.sewain.utils.NavUtil;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CHECKOUT_PRODUCTS = "productList";
    RecyclerView recyclerView;
    List cardList;
    private Cart_WishListAdapter mAdapter;
    private List<Product> mProducts;
    private TextView mTvAmountDue;
    //private Button checkout;
    private double mAmountDue = 0.0;
    private TextView mTvCheckoutDue;
    private DecimalFormat df = new DecimalFormat("#.##");


    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        mProducts = new ArrayList<>();


        mTvCheckoutDue = findViewById(R.id.tv_checkout_due);

        mTvAmountDue = findViewById(R.id.tv_amount_due);
        // checkout = findViewById(R.id.btn_checkout);
        recyclerView = findViewById(R.id.recycler_view22);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new Cart_WishListAdapter(AddCartActivity.this, mProducts);
        mAdapter.setOnItemClickListener(AddCartActivity.this);
        recyclerView.setAdapter(mAdapter);

        fetchCartProducts();

        FirebaseUser fuser;
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference addressRef =  FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("Address");

        addressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                address userAddress = snapshot.getValue(address.class);

                if (!snapshot.exists()){

                    Intent deliverAddrss = new Intent(AddCartActivity.this , DeliveryAdressActivity.class);
                    startActivity(deliverAddrss);

                    Toast.makeText(AddCartActivity.this, "Add Address To Continue", Toast.LENGTH_SHORT).show();

                } else {

                    Button btnCheckout = findViewById(R.id.btn_checkout);
                    btnCheckout.setOnClickListener(AddCartActivity.this);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchCartProducts() {
        String userId = getUserId();

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

                            double currentProductPrice = product.getProductPrice();
                            calcAmountDue(currentProductPrice);

                            mProducts.add(product);
                        }

                        mTvCheckoutDue.setText("R" + df.format(mAmountDue));
                        mAdapter = new Cart_WishListAdapter(AddCartActivity.this, mProducts);
                        mAdapter.setOnItemClickListener(AddCartActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void calcAmountDue(double productPrice) {
        mAmountDue += productPrice;
    }

    private String getUserId() {
        return Objects.requireNonNull(FirebaseAuth
                .getInstance()
                .getCurrentUser())
                .getUid();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_checkout) {

            FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

            String uid = mAuth.getUid();

            DatabaseReference checkRent = FirebaseDatabase.getInstance().getReference("Rented");
            checkRent.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()){

                        showDialog();


                    }else{

                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {

                            String key = currentSnapshot.getKey();
                            checkRent.child(key).child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (!snapshot.exists()){

                                        showDialog();

                                    }else{

                                        Toast.makeText(AddCartActivity.this, "You have rented a product,  Finish to do a purchase", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    
                                    

                                }
                            });

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void showDialog() {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.checkout_options, null);
        paymentOptions.findViewById(R.id.checkout_buy).setOnClickListener(this::checkout_buy);
        paymentOptions.findViewById(R.id.checkout_rent).setOnClickListener(this::checkout_rent);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setView(paymentOptions);

        builder
                .setCancelable(true)
                .create()
                .show();
    }

    private void checkout_buy(View view) {

        showDialogBuy();

    }

    private void checkout_rent(View view) {

        showDialogRent();

    }

    private void showDialogBuy() {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.checkout_payment_options, null);
        paymentOptions.findViewById(R.id.card_payment).setOnClickListener(this::cardPayment);
        paymentOptions.findViewById(R.id.paypal_payment).setOnClickListener(this::paypayPayment);
        paymentOptions.findViewById(R.id.google_payment).setOnClickListener(this::googlePayment);
        paymentOptions.findViewById(R.id.cod_payment).setOnClickListener(this::codPaymentBuy);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment method to buy");
        builder.setView(paymentOptions);


        builder
                .setCancelable(true)
                .create()
                .show();
    }

    private void showDialogRent() {

        View paymentOptions = this.getLayoutInflater().inflate(R.layout.checkout_payment_rent, null);
        paymentOptions.findViewById(R.id.card_payment_rent).setOnClickListener(this::cardPaymentRent);
        paymentOptions.findViewById(R.id.paypal_payment_rent).setOnClickListener(this::paypayPaymentRent);
        paymentOptions.findViewById(R.id.google_payment_rent).setOnClickListener(this::googlePayment);
        paymentOptions.findViewById(R.id.cod_payment_rent).setOnClickListener(this::codPaymentRent);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment method to rent");
        builder.setView(paymentOptions);

        builder
                .setCancelable(true)
                .create()
                .show();
    }


    private void cardPayment(View view) {
        toast("card payment");
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(this, CheckoutActivityJava.class, bundle);

    }

    private void paypayPayment(View view) {
        toast("paypal payment");
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(this, PayPalActivity.class, bundle);

    }

    private void cardPaymentRent(View view) {
        toast("You can Rent 1 item Only");
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(this, CheckoutActivityRentJava.class, bundle);

    }

    private void paypayPaymentRent(View view) {
        toast("You can Rent 1 item Only");
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
        NavUtil.moveTo(this, PayPalRentActivity.class, bundle);

    }

    private void codPaymentBuy(View view ) {
        toast("Cash On Delivery");

        Intent buyCOD = new Intent(AddCartActivity.this, CODActivity.class);
        startActivity(buyCOD);
    }

    private void codPaymentRent(View view ) {
        toast("Cash On Delivery");

        Intent rentCod = new Intent(AddCartActivity.this, CODRentActivity.class);
        startActivity(rentCod);

    }


    private void googlePayment(View view) {
        NavUtil.moveTo(this, GooglePayActivity.class, null);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}