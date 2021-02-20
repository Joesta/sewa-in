package za.co.robusttech.sewa_in.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.adapter.CartAdapter;
import za.co.robusttech.sewa_in.models.Cart;
import za.co.robusttech.sewa_in.models.Product;

public class AddCartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private CartAdapter mAdapter;
    private List<Product> mProducts;
    private TextView mTvAmountDue;
    private double mAmountDue = 0.0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        mProducts = new ArrayList<>();

        mTvAmountDue = findViewById(R.id.tv_amount_due);
        recyclerView = findViewById(R.id.recycler_view22);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        mAdapter = new CartAdapter(AddCartActivity.this, mProducts);
        mAdapter.setOnItemClickListener(AddCartActivity.this);
        recyclerView.setAdapter(mAdapter);

        fetchCartProducts();

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

                        String due = "Amt due: R" + mAmountDue;
                        mTvAmountDue.setText(due);
                        mAdapter = new CartAdapter(AddCartActivity.this, mProducts);
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

}