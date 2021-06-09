package za.co.robusttech.sewain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.adapter.Cart_WishListAdapter;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;
import za.co.robusttech.sewain.models.Rent;
import za.co.robusttech.sewain.utils.NavUtil;

public class NotificationActivity extends AppCompatActivity {

    private List<Product> mProducts;
    public static final String CHECKOUT_PRODUCTS = "productList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView productName = findViewById(R.id.rent_product_name);
        TextView productPrice = findViewById(R.id.rent_product_price);
        TextView amountPayed = findViewById(R.id.rent_amount_payed);
        TextView balanceAmount = findViewById(R.id.rent_amount_balance);
        TextView productBuyedOn = findViewById(R.id.rent_product_buyed_on);
        TextView productRentPrice = findViewById(R.id.rent_price);
        Button productRentPay = findViewById(R.id.rent_product_pay);
        Button productFullPay = findViewById(R.id.rent_pay_full);

        mProducts = new ArrayList<>();

        DatabaseReference rentRef = FirebaseDatabase.getInstance().getReference("Rented");

        rentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FirebaseUser auth;
                    auth = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = auth.getUid();

                    String rentID = dataSnapshot.getKey();

                    FirebaseDatabase.getInstance().getReference("Products").child(rentID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Product product = snapshot.getValue(Product.class);
                                    mProducts.add(product);
                                    Toast.makeText(NotificationActivity.this, product.getProductId(), Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                    DatabaseReference rentIdRef = FirebaseDatabase.getInstance().getReference("Rented").child(rentID).child(userId);

                    rentIdRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Rent rent = snapshot.getValue(Rent.class);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String currentDate = simpleDateFormat.format(new Date());

                            if (!snapshot.exists()){

                            }else{

                                productName.setText(rent.getProductName());
                                productPrice.setText(rent.getProductPrice());
                                amountPayed.setText(rent.getAmountPayed());
                                balanceAmount.setText(rent.getAmountBalance());
                                productBuyedOn.setText(rent.getFirstRentTime());
                                productRentPrice.setText(rent.getPerRent());


                                productRentPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(CHECKOUT_PRODUCTS, (Serializable) mProducts);
                                        NavUtil.moveTo(NotificationActivity.this, CheckoutActivityRentPayJava.class, bundle);


                                    }
                                });

                                productFullPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

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



    }
}