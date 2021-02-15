package za.co.robusttech.sewa_in.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Products;

public class ItemDetailActivity extends AppCompatActivity {

    ImageSlider mainSlider;
    TextView name , orPrice , price , dlTime , description , discount  , ratings;
    TextView detainCategory , detainName,detainSellers,detainAvailability,detaiId,detainPrice,detainOriginalPrice,detainDeliveryTime,detainRatings,detainDiscount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        String productName = getIntent().getStringExtra("productName");
        String productRatings = getIntent().getStringExtra("productRatings");
        String productPrice = getIntent().getStringExtra("productPrice");
        String productDeliveryTime = getIntent().getStringExtra("productDeliveryTime");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productDiscount = getIntent().getStringExtra("productDiscount");
        String productId = getIntent().getStringExtra("productId");
        String productImage = getIntent().getStringExtra("productImage");

        name = findViewById(R.id.item_name);
        orPrice = findViewById(R.id.original_price_txt);
        price = findViewById(R.id.price_txt);
        dlTime = findViewById(R.id.delivery_expected);
        discount = findViewById(R.id.discount);
        mainSlider = findViewById(R.id.image_slider);
        ratings = findViewById(R.id.productRatings);


        detainCategory = findViewById(R.id.product_details_category);
        detainName = findViewById(R.id.product_details_name);
        detainSellers = findViewById(R.id.product_details_Sellers);
        detainAvailability = findViewById(R.id.product_details_availability);

        detaiId = findViewById(R.id.product_details_id);
        detainPrice = findViewById(R.id.product_details_price);
        detainOriginalPrice = findViewById(R.id.product_details_or_price);
        detainDeliveryTime = findViewById(R.id.product_details_deliveryTime);
        detainRatings = findViewById(R.id.product_details_ratings);
        description = findViewById(R.id.product_details_body);
        final LinearLayout add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
        detainDiscount = findViewById(R.id.product_details_discount);


        final List<SlideModel> remoteimages = new ArrayList<>();


        assert productId != null;

        FirebaseDatabase.getInstance().getReference("Products").child(productId).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    remoteimages.add(new SlideModel(dataSnapshot.child("url").getValue().toString(), ScaleTypes.FIT));
                    mainSlider.setImageList(remoteimages, ScaleTypes.FIT);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Products").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Products products = snapshot.getValue(Products.class);

                String productName = products.getProductName();
                String productRatings = products.getProductRatings();
                String productPrice = products.getProductPrice();
                String productDeliveryTime = products.getProductDeliveryTime();
                String productDescription = products.getProductDesciption();
                String productDiscount = products.getProductDiscount();
                final String productId = products.getProductId();
                String productCategory = products.getProductCategory();
                String productSeller = products.getProductSeller();
                String productAvailability = products.getProductAvailability();
                String productOriginalPrice = products.getProductOriginalPrice();
                String productNameFull = products.getProductNameFull();
                String productDescriptionFull = products.getProductDescriptionFull();

                detainCategory.setText("Category : "+ productCategory);
                detainName.setText("Product Name : "+ productNameFull);
                detainSellers.setText("Seller : "+ productSeller);
                detainAvailability.setText("Availability : "+productAvailability);
                detaiId.setText("Id : "+productId);
                detainPrice.setText("Price : "+productPrice);
                detainOriginalPrice.setText("Original Price : "+productOriginalPrice);
                detainDeliveryTime.setText("Delivery Time : "+productDeliveryTime);
                detainRatings.setText("Ratings : "+productRatings);
                detainDiscount.setText("Discount : "+productDiscount);

                description.setText(productDescriptionFull);
                name.setText(productNameFull);
                orPrice.setText(productOriginalPrice);
                price.setText(productPrice);
                dlTime.setText(productDeliveryTime);
                discount.setText(productDiscount);
                ratings.setText(productRatings);

                add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("inCart").child(productId).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Cart").child(productId)
                                .child("onCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);


                        Intent intent = new Intent(ItemDetailActivity.this, AddCartActivity.class);
                        intent.putExtra("productId", productId);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}