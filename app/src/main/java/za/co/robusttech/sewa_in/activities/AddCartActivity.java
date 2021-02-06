package za.co.robusttech.sewa_in.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.adapter.CartAdapter;
import za.co.robusttech.sewa_in.adapter.CartViewHolder;
import za.co.robusttech.sewa_in.models.Products;

public class AddCartActivity extends AppCompatActivity  {

    FirebaseRecyclerOptions<Products> Products;

    RecyclerView recyclerView ;
    private CartAdapter mAdapter;
    private List<Products> mProducts;

    FirebaseRecyclerAdapter<Products, CartViewHolder> adapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);


        recyclerView = findViewById(R.id.recycler_view22);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mProducts = new ArrayList<>();

        FirebaseUser firebaseUser;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    final DatabaseReference postKey = FirebaseDatabase.getInstance().getReference("Products")
                            .child(postSnapshot.getKey());
                    DatabaseReference cart = FirebaseDatabase.getInstance().getReference("Cart")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("inCart");
                    cart.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final String key = dataSnapshot.getKey();

                                postKey.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Products products = snapshot.getValue(Products.class);
                                        for (DataSnapshot product : snapshot.getChildren()){
                                            assert key != null;
                                            assert products != null;
                                            if (key.equals(products.getProductId())){

                                                products.setKey(product.getKey());
                                                mProducts.add(products);
                                                Set<Products> set = new HashSet<>(mProducts);
                                                mProducts.clear();
                                                mProducts.addAll(set);

                                            }
                                        }
                                        mAdapter.notifyDataSetChanged();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mAdapter = new CartAdapter(AddCartActivity.this, mProducts);
        mAdapter.setOnItemClickListener(AddCartActivity.this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause(){
        super.onPause();
        mAdapter.notifyDataSetChanged();

    }

}