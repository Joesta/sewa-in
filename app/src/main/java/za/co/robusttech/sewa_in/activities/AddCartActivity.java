package za.co.robusttech.sewa_in.activities;


import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.adapter.Cart_WishListAdapter;
import za.co.robusttech.sewa_in.models.Cart;
import za.co.robusttech.sewa_in.models.Product;
import za.co.robusttech.sewa_in.utils.NavUtil;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    List cardList;
    private Cart_WishListAdapter mAdapter;
    private List<Product> mProducts;
    private TextView mTvAmountDue;
    //private Button checkout;
    private double mAmountDue = 0.0;
    private TextView mTvCheckoutDue;
    public static final String CHECKOUT_PRODUCTS = "productList";

    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        mProducts = new ArrayList<>();

        Button btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(this);

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

//        checkout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent deliverAddrss = new Intent(AddCartActivity.this , DeliveryAdressActivity.class);
//                startActivity(deliverAddrss);
//
//
//            }
//        });

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

                        mTvCheckoutDue.setText("R" + mAmountDue);
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
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_checkout) {
            showDialog();
        }
    }

    private void showDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment method");
        builder.setPositiveButton("Cash", (dialog, which) -> {
            toast("cash payment");
        }).setNegativeButton("Card", (dialog, which) -> {
            toast("card payment");
            Bundle bundle = new Bundle();
            bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable)mProducts);
            NavUtil.moveTo(this, CheckoutActivityJava.class, bundle);
        });
        builder
                .setCancelable(true)
                .create()
                .show();
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}