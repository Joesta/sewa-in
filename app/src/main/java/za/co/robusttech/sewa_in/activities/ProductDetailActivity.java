package za.co.robusttech.sewa_in.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import za.co.robusttech.sewa_in.R;
import za.co.robusttech.sewa_in.models.Cart;
import za.co.robusttech.sewa_in.models.Product;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef;
    private ImageSlider mainSlider;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private TextView mTvProductDiscount;
    private TextView mTvProductName;
    private TextView mTvProductCategory;
    private TextView mTvProductQty;
    private ImageButton mBtnQuantityAdd;
    private ImageButton mBtnQuantityMinus;
    private Cart cart = new Cart();
    private Product mProduct;
    private LinearLayout add_to_cart_btn;
    private String mUserId;
    private int mProductQty = 1;
    private double mProductPrice = 0.0;

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
        mTvProductName = findViewById(R.id.tv_product_name);
        mTvProductPrice = findViewById(R.id.tv_product_price);
        mTvProductDescription = findViewById(R.id.tv_product_description);

        mTvProductQty = findViewById(R.id.tv_product_quantity_display);
        mBtnQuantityAdd = findViewById(R.id.btn_quantity_add);
        mBtnQuantityMinus = findViewById(R.id.btn_quantity_minus);

        mBtnQuantityAdd.setOnClickListener(this);
        mBtnQuantityMinus.setOnClickListener(this);

    }

    private void setProductDetails() {

        mTvProductName.setText(mProduct.getProductName());
        mTvProductPrice.setText("R" + mProduct.getProductPrice());
        mTvProductDescription.setText(mProduct.getProductDescription());
//        mTvProductQty.setText(mProduct.getProductQuantity());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_to_cart_btn:
                addProductToCart();
                break;
            case R.id.btn_quantity_add:
                addProductQuantity();
                showProductQuantity();
                break;
            case R.id.btn_quantity_minus:
                minusProductQuantity();
                showProductQuantity();
                break;
            default:
                break;
        }
    }

    private void minusProductQuantity() {
        if (mProductQty == 1)
           return;
        mProductQty -= 1;
    }

    private void addProductQuantity() {
        mProductQty += 1;
    }

    private void showProductQuantity() {
        mTvProductQty.setText(String.valueOf(mProductQty));
    }

    private void updateProductPriceAndQuantity() {
        mProductPrice = mProduct.getProductPrice();
        double newPrice = mProductPrice * mProductQty;

        mProduct.setProductPrice(newPrice);
        mProduct.setProductQuantity(mProductQty);
    }

    private void addProductToCart() {
        updateProductPriceAndQuantity();
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

    }
}