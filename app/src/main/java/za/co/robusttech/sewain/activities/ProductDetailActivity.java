package za.co.robusttech.sewain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Comment;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.models.WishList;
import za.co.robusttech.sewain.models.profile;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef;
    private ImageSlider mainSlider;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private TextView mTvProductDiscount;
    private TextView mTvProductName;
    private TextView tv_product_review;
    private TextView mTvProductQty;
    private TextView tv_product_review_title;
    private TextView username_review;

    private ToggleButton heart;
    private TextView  btn_add_direct_review;
    private ImageButton mBtnQuantityAdd;
    private ImageButton mBtnQuantityMinus;
    private Cart cart = new Cart();
    private WishList wishList = new WishList();

    private Product mProduct;
    private LinearLayout add_to_cart_btn;
    private String mUserId;
    private int mProductQty = 1;
    private double mProductNewPrice = 0.0;
    DatabaseReference commentsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        TextView see_all = findViewById(R.id.btn_see_all);
        btn_add_direct_review = findViewById(R.id.btn_add_direct_review);
        tv_product_review = findViewById(R.id.tv_product_review);
        tv_product_review_title = findViewById(R.id.tv_product_review_title);
        username_review = findViewById(R.id.username_review);

        mRef = mDatabase.getReference();
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        mProduct = (Product) getIntent().getSerializableExtra(HomeActivity.PRODUCT);
        assert mProduct != null;

        btn_add_direct_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this , CommentUpdateActivity.class);
                intent.putExtra("productId", mProduct.getProductId());
                startActivity(intent);


            }
        });

        commentsRef.child(mProduct.getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    if (comment.getComment() != null){
                        tv_product_review.setText(comment.getComment());
                        tv_product_review_title.setText(comment.getCommentTitle());

                        FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getPublisher())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        profile user = snapshot.getValue(profile.class);
                                        username_review.setText(user.getName());

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }else{
                        tv_product_review.setText("No Review for this product");
                        tv_product_review_title.setVisibility(View.GONE);
                        username_review.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this , CommentsActivity.class);
                intent.putExtra("productId", mProduct.getProductId());
                startActivity(intent);
            }
        });

        initUI();
        setProductDetails();
        add_to_cart_btn.setOnClickListener(this);

        final List<SlideModel> productImages = new ArrayList<>();


        productImages.add(new SlideModel(mProduct.getProductImageUrl(), ScaleTypes.FIT));
        mainSlider.setImageList(productImages, ScaleTypes.FIT);

        heart = findViewById(R.id.heart_Item);
        heart.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_like));

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products Likes")
                .child(mProduct.getProductId())
                .child("Likers");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    heart.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_liked));
                }else{
                    heart.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_like));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    heart.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_liked));
                    wishList.setProduct(mProduct);
                    wishList.setCustomerId(mUserId);
                    mRef
                            .child("WishList")
                            .child(mUserId)
                            .child(mProduct.getProductId())
                            .setValue(wishList);

                    FirebaseDatabase.getInstance().getReference().child("Products Likes")
                            .child(mProduct.getProductId())
                            .child("Likers")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    Toast.makeText(ProductDetailActivity.this, "Added To WishList", Toast.LENGTH_SHORT).show();

                }else{

                    heart.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_like));
                    mRef
                            .child("WishList")
                            .child(mUserId)
                            .child(mProduct.getProductId())
                            .removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Products Likes")
                            .child(mProduct.getProductId())
                            .child("Likers")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                    Toast.makeText(ProductDetailActivity.this, "Removed To WishList", Toast.LENGTH_SHORT).show();

                }
            }
        });


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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_to_cart_btn:
                addProductToCart();
                break;
            case R.id.btn_quantity_add:
                addProductQuantity();
                break;
            case R.id.btn_quantity_minus:
                minusProductQuantity();
                break;
            default:
                break;
        }
    }

    private void minusProductQuantity() {
        if (mProductQty == 1)
           return;
        mProductQty -= 1;
        showProductQuantity();
    }

    private void addProductQuantity() {
        mProductQty += 1;
        showProductQuantity();
    }

    private void showProductQuantity() {
        mTvProductQty.setText(String.valueOf(mProductQty));
    }

    private void calcAmountDue() {
        double productPrice = mProduct.getProductPrice();
         mProductNewPrice = productPrice * mProductQty;
    }

    private void updateProduct() {
        calcAmountDue();
        mProduct.setProductPrice(mProductNewPrice);
        mProduct.setProductQuantity(mProductQty);
    }

    private void resetPrice() {mProductNewPrice = 0.0;}

    private void addProductToCart() {
        updateProduct();
        resetPrice();
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

        startActivity(new Intent(this, HomeActivity.class));

    }
}