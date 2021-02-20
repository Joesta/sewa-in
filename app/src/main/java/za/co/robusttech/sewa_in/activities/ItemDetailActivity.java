package za.co.robusttech.sewa_in.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Cart;
import za.co.robusttech.sewa_in.models.Product;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//    TextView name , orPrice , price , dlTime , description , discount  , ratings;
//    TextView detainCategory , detainName,detainSellers,detainAvailability,detaiId,detainPrice,detainOriginalPrice,detainDeliveryTime,detainRatings,detainDiscount;
    DatabaseReference mRef;
    private ImageSlider mainSlider;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private TextView mTvProductDiscount;
    private TextView mTvProductName;
    private TextView mTvProductCategory;
    private Cart cart = new Cart();
    private Product mProduct;
    private LinearLayout add_to_cart_btn;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mRef = mDatabase.getReference();
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProduct = (Product) getIntent().getSerializableExtra(HomeActivity.PRODUCT);
        assert mProduct != null;

        initUI();
        setProductDetails();
        add_to_cart_btn.setOnClickListener(this);

        final List<SlideModel> productImages = new ArrayList<>();


        productImages.add(new SlideModel(mProduct.getProductImageUrl(), ScaleTypes.FIT));
        mainSlider.setImageList(productImages, ScaleTypes.FIT);

    }

    private void initUI() {
        mainSlider = findViewById(R.id.image_slider);
        mTvProductPrice = findViewById(R.id.tv_product_price);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);

    }

    private void setProductDetails() {
        String productDescription = "Description \t: " + mProduct.getProductDescription();
        String productCategory = "Category \t: " + mProduct.getProductCategory();
        String productPrice = "Price \t: " + mProduct.getProductPrice();
        String productName = "Name \t: " + mProduct.getProductName();
        String productDiscount = "Discount \t:" + mProduct.getProductDiscount();

//        mTvProductCategory.setText(productCategory);
//        mTvProductDescription.setText(productDescription);
//        mTvProductDiscount.setText(productDiscount);
//        mTvProductName.setText(productName);
//        mTvProductPrice.setText(productPrice);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_to_cart_btn:
                addProductToCart();
                break;
            default:
                break;
        }
    }

    private void addProductToCart() {

        cart.setProduct(mProduct);
        cart.setCustomerId(mUserId);
        mRef
                .child("Cart")
                .child(mUserId)
                .child(mProduct.getProductId())
                .setValue(cart)
                .addOnCompleteListener(this, task -> {
                    if (task.isComplete()) {
                        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add product to cart", Toast.LENGTH_LONG).show();
                    }
                });

        //        cart.add(mProduct);
//
//
//        FirebaseDatabase.getInstance().getReference().child("CustomerCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(cart);
//
//
//        FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("inCart").child(productId).setValue(true);
//
//        FirebaseDatabase.getInstance().getReference().child("Cart").child(productId)
//                .child("onCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
//
//
//        Intent intent = new Intent(ItemDetailActivity.this, AddCartActivity.class);
//        intent.putExtra("productId", productId);
//        startActivity(intent);

    }
}