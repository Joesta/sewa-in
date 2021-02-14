package za.co.robusttech.sewa_in.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Products;

public class ItemDetailActivity extends AppCompatActivity {

    ImageSlider mainSlider;
    TextView name, orPrice, price, dlTime, description, discount, ratings;
    TextView detainCategory, detainName, detainAvailability, detainPrice, detainOriginalPrice, detainDeliveryTime, detainRatings, detainDiscount;

    private Products product;
    private String productId;
    private LinearLayout add_to_cart_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        product = (Products) getIntent().getSerializableExtra("product");

        if (product != null) {
            productId = product.getProductId();
        }

        price = findViewById(R.id.price_txt);
        mainSlider = findViewById(R.id.image_slider);
        ratings = findViewById(R.id.productRatings);


//        detainCategory = findViewById(R.id.product_details_category);
//        detainName = findViewById(R.id.product_details_name);
//        detainAvailability = findViewById(R.id.product_details_availability);
//        detainPrice = findViewById(R.id.product_details_price);
//        detainOriginalPrice = findViewById(R.id.product_details_or_price);
//        detainDeliveryTime = findViewById(R.id.product_details_deliveryTime);
//        detainRatings = findViewById(R.id.product_details_ratings);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
//        detainDiscount = findViewById(R.id.product_details_discount);


        final List<SlideModel> imageList = new ArrayList<>();

        // for testing
        imageList.add(new SlideModel(R.drawable.office_chair, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.office_chair, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.office_chair, ScaleTypes.FIT));
        mainSlider.setImageList(imageList, ScaleTypes.FIT);

//        FirebaseDatabase.getInstance().getReference("Products").child(productId).child("images").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    imageList.add(new SlideModel(dataSnapshot.child("url").getValue().toString(), ScaleTypes.FIT));
//                    mainSlider.setImageList(imageList, ScaleTypes.FIT);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //setProductDetails();

    }


    private void setProductDetails() {
        final String productId = product.getProductId();

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child("inCart").child(productId).setValue(true);
//
//                FirebaseDatabase.getInstance().getReference().child("Cart").child(productId)
//                        .child("onCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
//
//
//                Intent intent = new Intent(ItemDetailActivity.this, AddCartActivity.class);
//                intent.putExtra("productId", productId);
//                startActivity(intent);

            }
        });
    }


}