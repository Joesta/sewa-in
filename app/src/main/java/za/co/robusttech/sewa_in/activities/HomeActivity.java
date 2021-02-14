package za.co.robusttech.sewa_in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Products;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView;
    private EditText mSearchField;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;
    private List<Products> product;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridView = findViewById(R.id.grid_layout);
        gridLoad();

        NavigationView navigationView;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.authText);
        LinearLayout linearLayout = headerView.findViewById(R.id.authLaout);
        //  navUsername.setText("Your Text Here");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorblack));

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mSearchField = findViewById(R.id.search_field);
        ImageButton mSearchBtn = findViewById(R.id.search_btn);

        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
                gridView.setVisibility(View.GONE);
            }
        });

    }

    private void gridLoad() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, ItemDetailActivity.class);

                intent.putExtra("product", product.get(position));

                startActivity(intent);

            }
        });


        product = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {

                    Products pro = data.getValue(Products.class);

                    product.add(pro);

                    CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, product);
                    gridView.setVisibility(View.VISIBLE);
                    gridView.setAdapter(customAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void firebaseUserSearch(String searchText) {
        Toast.makeText(HomeActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Products");

        Query firebaseSearchQuery = mUserDatabase.orderByChild("productName").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(firebaseSearchQuery, Products.class).build();
        FirebaseRecyclerAdapter<Products, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull final Products model) {
                holder.setDetails(HomeActivity.this, model.getProductName(), model.getProductImage(), model.getProductRatings(), model.getProductPrice(), model.getProductDeliveryTime(), model.getProductId(), model.getProductDiscount(), model.getProductCategory(), model.getProductDesciption());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);

                        intent.putExtra("productId", model.getProductId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
                return new UsersViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_My_store:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_Orders:
                Toast.makeText(this, "My Orders", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.nav_My_Rewards:
                Toast.makeText(this, "My Rewards", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_Cart:

                Intent cart = new Intent(HomeActivity.this, AddCartActivity.class);
                startActivity(cart);
                return true;

            case R.id.nav_My_Wishlist:
                Toast.makeText(this, "My Wishlist", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.nav_My_account:
                Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.nav_sign_out:

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setDetails(Context context, String productName, String productImage, String productRatings, String productPrice, String productDeliveryTime, String productId, String productDiscount, String productCategory, String productDesciption) {

            TextView name = mView.findViewById(R.id.productName);
            ImageView image = mView.findViewById(R.id.product_image);
            TextView ratings = mView.findViewById(R.id.productRatings);
            TextView price = mView.findViewById(R.id.productPrice);
            TextView deliveryTime = mView.findViewById(R.id.productDeliveryTime);
            TextView discount = mView.findViewById(R.id.productDiscount);

            name.setText(productName);
            Glide.with(context).load(productImage).into(image);
            ratings.setText(productRatings);
            price.setText(productPrice);
            deliveryTime.setText(productDeliveryTime);
            discount.setText(productDiscount);


        }
    }

    private class CustomAdapter extends BaseAdapter {
        int total;
        private Context mContext;
        private List<Products> products;

        public CustomAdapter(Context context, List<Products> products) {
            mContext = context;
            this.products = products;
        }

        @Override
        public int getCount() {

            //  return
            total = products.size();

            return total;
        }

        @Override
        public Object getItem(int i) {
            return products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.horizontal_product_item, null);
            CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder();
            viewHolder.image = view1.findViewById(R.id.h_s_product_image);
            viewHolder.name = view1.findViewById(R.id.h_s_product_title);
            viewHolder.description = view1.findViewById(R.id.h_s_product_description);
            viewHolder.price = view1.findViewById(R.id.h_s_product_price);


            if (i <= products
                    .size()) {
                Products produc = products.get(i);
                Glide.with(HomeActivity.this).load(produc.getProductImage()).into(viewHolder.image);
                viewHolder.name.setText("product name");
                viewHolder.description.setText("description");
                viewHolder.price.setText("product price");

            }
            return view1;

        }

        class ViewHolder {
            ImageView image;
            TextView name, description, price;
        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}