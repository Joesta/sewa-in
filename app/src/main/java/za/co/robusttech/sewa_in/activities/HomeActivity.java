package za.co.robusttech.sewa_in.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.adapter.sub_topic_adapter;
import za.co.robusttech.sewa_in.models.Product;
import za.co.robusttech.sewa_in.models.home_sub;
import za.co.robusttech.sewa_in.models.profile;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        GridView gridView;
    private EditText mSearchField;
    private RecyclerView mResultList;
    private GridLayout gridLayout;
    ImageSlider mainslider;


    ImageView hg_img , hg_img2;
    TextView hg_title1 ,hg_title2 , hg_desc1 , hg_desc2 , hg_price1 , hg_prce2;

    private DatabaseReference mUserDatabase;
    private List<Product> products;
    private DatabaseReference mDatabaseRef;

    private List<home_sub> homeSub = new ArrayList<>();
    private RecyclerView subRecyclerView;
    private sub_topic_adapter subAdapter;

    public static final String PRODUCT = "product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hg_img = findViewById(R.id.h_g_product_image);
        hg_img2 = findViewById(R.id.h_g_product_image2);
        hg_title1 = findViewById(R.id.h_g_product_title);
        hg_title2 = findViewById(R.id.h_g_product_title2);
        hg_desc1 = findViewById(R.id.h_g_product_description);
        hg_desc2 = findViewById(R.id.h_g_product_description2);
        gridLayout = findViewById(R.id.gridLayoutId);
        hg_price1 = findViewById(R.id.h_g_product_price);
        hg_prce2 = findViewById(R.id.h_g_product_price2);

        findViewById(R.id.floating_action_button).setOnClickListener(v -> {
            startActivity(new Intent(this, AddCartActivity.class));
        });

        mainslider=(ImageSlider)findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("product bannner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren())
                            remoteimages.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));

                        mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                        mainslider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Toast.makeText(getApplicationContext(),remoteimages.get(i).getImageUrl().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        subRecyclerView = findViewById(R.id.rc_for_sub);
        subRecyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.HORIZONTAL));
        subAdapter = new sub_topic_adapter(homeSub, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        subRecyclerView.setLayoutManager(horizontalLayoutManager);
        subRecyclerView.setAdapter(subAdapter);
        populateSubList();

        gridView = findViewById(R.id.grid_layout);
        gridLoad();

        NavigationView navigationView;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout linearLayout = headerView.findViewById(R.id.authLaout);
        //  navUsername.setText("Your Text Here");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mSearchField = findViewById(R.id.search_field);
        ImageButton mSearchBtn = findViewById(R.id.search_btn);

        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CircleImageView navImage =  headerView.findViewById(R.id.profileimagena);
                TextView navName = headerView.findViewById(R.id.authTextn);
                profile user = snapshot.getValue(profile.class);
                navName.setText(user.getName());
                if (user.getImageURL().equals("default")){
                    navImage.setImageResource(R.drawable.profile);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(navImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                subRecyclerView.setVisibility(View.GONE);
            }
        });

    }

    private void populateSubList() {

        home_sub topic1 = new home_sub("All", R.drawable.cod_icon);
        home_sub topic2 = new home_sub("Pantry", R.drawable.pantry);
        home_sub topic3 = new home_sub("Mobiles", R.drawable.mobiles);
        home_sub topic4 = new home_sub("Fashion", R.drawable.fashion);
        home_sub topic5 = new home_sub("Appliances", R.drawable.appliances);
        home_sub topic6 = new home_sub("Watches", R.drawable.watches);
        home_sub topic7 = new home_sub("Health & Beauty", R.drawable.health_beauty);
        home_sub topic8 = new home_sub("Pc Components", R.drawable.pc_components);
        home_sub topic9 = new home_sub("Fragrances", R.drawable.fragrances);

        homeSub.add(topic1);
        homeSub.add(topic2);
        homeSub.add(topic3);
        homeSub.add(topic4);
        homeSub.add(topic5);
        homeSub.add(topic6);
        homeSub.add(topic7);
        homeSub.add(topic8);
        homeSub.add(topic9);


        subAdapter.notifyDataSetChanged();
    }

    private void gridLoad() {

        String productName = getIntent().getStringExtra("productName");

        if (productName != null ){

            if (productName.equals("All")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);

                            products.add(product);
                            CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                            gridView.setVisibility(View.VISIBLE);
                            gridLayout.setVisibility(View.VISIBLE);
                            subRecyclerView.setVisibility(View.VISIBLE);
                            gridView.setAdapter(customAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Mobiles")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);

                            assert product != null;
                            if (product.getProductCategory().equals("Mobiles")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if (productName.equals("Pantry")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Pantry")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Pc Components")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Pc Components")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Fashion")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Fashion")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Appliances")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Appliances")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Watches")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Watches")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Health & Beauty")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Health & Beauty")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            if (productName.equals("Fragrances")){

                products = new ArrayList<>();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {

                            Product product = data.getValue(Product.class);
                            assert product != null;
                            if (product.getProductCategory().equals("Fragrances")){

                                products.add(product);
                                CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                                gridView.setVisibility(View.VISIBLE);
                                gridLayout.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.VISIBLE);
                                gridView.setAdapter(customAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        }else{

            products = new ArrayList<>();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot data : snapshot.getChildren()) {


                        Product product = data.getValue(Product.class);
                        products.add(product);
                        CustomAdapter customAdapter = new CustomAdapter(HomeActivity.this, products);
                        gridView.setVisibility(View.VISIBLE);
                        gridLayout.setVisibility(View.VISIBLE);
                        subRecyclerView.setVisibility(View.VISIBLE);
                        gridView.setAdapter(customAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra(PRODUCT, products.get(position));
                startActivity(intent);
            }
        });


    }

    private void firebaseUserSearch(String searchText) {
        Toast.makeText(HomeActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Products");

        Query firebaseSearchQuery = mUserDatabase.orderByChild("productName").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(firebaseSearchQuery, Product.class).build();
        FirebaseRecyclerAdapter<Product, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull final Product product) {
                holder.setProductDetails(HomeActivity.this, product);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                        intent.putExtra("product", product);
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
        int id = menuItem.getItemId();
        switch (menuItem.getItemId()) {
            case R.id.nav_My_store:
                Intent home = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(home);
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
                Intent wishList = new Intent(HomeActivity.this, WishListActivity.class);
                startActivity(wishList);
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


        public void setProductDetails(Context context, Product product) {

            TextView name = mView.findViewById(R.id.productName);
            ImageView image = mView.findViewById(R.id.product_image);
            TextView ratings = mView.findViewById(R.id.productRatings);
            TextView price = mView.findViewById(R.id.productPrice);
            TextView deliveryTime = mView.findViewById(R.id.productDeliveryTime);
            TextView discount = mView.findViewById(R.id.productDiscount);

            String productName = product.getProductName();
            String productImageUrl = product.getProductImageUrl();
            String productRating = String.valueOf(product.getProductRating());
            String productPrice = String.valueOf(product.getProductPrice());
            String productDiscount = String.valueOf(product.getProductDiscount());

            name.setText(productName);
            Glide.with(context).load(productImageUrl).into(image);
            ratings.setText(productRating);
            price.setText(productPrice);
            deliveryTime.setText("No delivery time");
            discount.setText(productDiscount);


        }
    }

    private class CustomAdapter extends BaseAdapter {
        int total;
        private Context mContext;
        private List<Product> products;

        public CustomAdapter(Context context, List<Product> products) {
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
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.horizontal_product_item, null);
            CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder();
            viewHolder.image = view1.findViewById(R.id.h_s_product_image);
            viewHolder.name = view1.findViewById(R.id.h_s_product_title);
            viewHolder.description = view1.findViewById(R.id.h_s_product_description);
            viewHolder.price = view1.findViewById(R.id.h_s_product_price);

            if (i == 18){

                Product product = products.get(i);
                Glide.with(HomeActivity.this).load(product.getProductImageUrl()).into(hg_img);
                hg_title1.setText(product.getProductName());
                hg_desc1.setText(product.getProductDescription());
                hg_price1.setText(String.valueOf(product.getProductPrice()));

            }
            if (i == 19){

                Product product = products.get(i);
                Glide.with(HomeActivity.this).load(product.getProductImageUrl()).into(hg_img2);
                hg_title2.setText(product.getProductName());
                hg_desc2.setText(product.getProductDescription());
                hg_prce2.setText(String.valueOf(product.getProductPrice()));

            }

            if (i <= 17){

                Product product = products.get(i);
                Glide.with(HomeActivity.this).load(product.getProductImageUrl()).into(viewHolder.image);
                viewHolder.name.setText(product.getProductName());
                viewHolder.description.setText(product.getProductDescription());
                viewHolder.price.setText(String.valueOf(product.getProductPrice()));
            }

            hg_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(18));
                    startActivity(intent);

                }
            });

            hg_img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(19));
                    startActivity(intent);

                }
            });



            hg_title1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(18));
                    startActivity(intent);

                }
            });

            hg_desc1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(18));
                    startActivity(intent);

                }
            });

            hg_price1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(18));
                    startActivity(intent);

                }
            });

            hg_title2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(19));
                    startActivity(intent);

                }
            });

            hg_desc2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(19));
                    startActivity(intent);

                }
            });

            hg_prce2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                    intent.putExtra(PRODUCT, products.get(19));
                    startActivity(intent);

                }
            });




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