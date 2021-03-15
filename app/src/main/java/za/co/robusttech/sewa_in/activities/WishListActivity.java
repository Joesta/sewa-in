package za.co.robusttech.sewa_in.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import za.co.robusttech.sewa_in.adapter.Cart_WishListAdapter;
import za.co.robusttech.sewa_in.models.Cart;
import za.co.robusttech.sewa_in.models.Product;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    private Cart_WishListAdapter mAdapter;
    private List<Product> mProducts;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        mProducts = new ArrayList<>();



        recyclerView = findViewById(R.id.recycler_view42);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new Cart_WishListAdapter(WishListActivity.this, mProducts);
        mAdapter.setOnItemClickListener(WishListActivity.this);
        recyclerView.setAdapter(mAdapter);

        fetchCartProducts();

    }

    private void fetchCartProducts() {
        String userId = getUserId();

        FirebaseDatabase
                .getInstance()
                .getReference("WishList")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) return;

                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                            Cart cart = currentSnapshot.getValue(Cart.class);
                            assert cart != null;
                            Product product = cart.getProduct();

                            mProducts.add(product);
                        }

                        mAdapter = new Cart_WishListAdapter(WishListActivity.this, mProducts);
                        mAdapter.setOnItemClickListener(WishListActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
        }
    }
}