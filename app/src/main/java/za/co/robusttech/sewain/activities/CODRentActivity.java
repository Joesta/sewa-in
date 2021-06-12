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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.models.Cart;
import za.co.robusttech.sewain.models.Product;

public class CODRentActivity extends AppCompatActivity {

    TextView productName , productPrice , productAriveOn , rentPayResult;

    Button buyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_o_d_rent);

        productName = findViewById(R.id.nameResultCod_rent);
        productPrice = findViewById(R.id.priceCodResult_rent);
        productAriveOn = findViewById(R.id.expectedArriveResult_rent);
        buyProduct = findViewById(R.id.buyProductCod_rent);
        rentPayResult = findViewById(R.id.rentPayResult);


        String userId = getUserId();

        FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) return;

                        for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                            Cart cart = currentSnapshot.getValue(Cart.class);
                            assert cart != null;
                            Product product = cart.getProduct();

                            Double price = product.getProductPrice();
                            Double Rentprice = product.getProductPrice()/10;

                            productName.setText(product.getProductName());
                            productPrice.setText(String.valueOf(price));
                            rentPayResult.setText(String.valueOf(Rentprice));

                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                            String firstTimeOutput = simpleDateFormat1.format(new Date());
                            String date1 = firstTimeOutput;
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                            Calendar calendar12 = Calendar.getInstance();
                            try {
                                calendar12.setTime(simpleDateFormat2.parse(date1));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            calendar12.add(Calendar.DATE, 5);//140
                            SimpleDateFormat simpleDateFormat32 = new SimpleDateFormat("dd-MM-yyyy");
                            String arriveDate = simpleDateFormat32.format(calendar12.getTime());

                            productAriveOn.setText(arriveDate);

                            buyProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String userId = getUserId();

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference("Cart")
                                            .child(userId)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (!snapshot.exists()) return;

                                                    for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                                                        Cart cart = currentSnapshot.getValue(Cart.class);
                                                        assert cart != null;
                                                        Product product = cart.getProduct();

                                                        DatabaseReference codRef = FirebaseDatabase.getInstance().getReference("COD Rent").child(userId);
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                        String currentDate = simpleDateFormat.format(new Date());

                                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                                                        String firstTimeOutput = simpleDateFormat1.format(new Date());
                                                        String date1 = firstTimeOutput;
                                                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                                                        Calendar calendar12 = Calendar.getInstance();
                                                        try {
                                                            calendar12.setTime(simpleDateFormat2.parse(date1));
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        calendar12.add(Calendar.DATE, 5);//140
                                                        SimpleDateFormat simpleDateFormat32 = new SimpleDateFormat("dd-MM-yyyy");
                                                        String expectedArriveDate = simpleDateFormat32.format(calendar12.getTime());

                                                        String productId = codRef.push().getKey();

                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("productId", product.getProductId());
                                                        map.put("productBuyer", userId);
                                                        map.put("productOrdered", currentDate);
                                                        map.put("productPrice", product.getProductPrice());
                                                        map.put("productName", product.getProductName());
                                                        map.put("poductRent", "true");
                                                        map.put("priceRent", String.valueOf(Rentprice));
                                                        map.put("expectedArriveDate", expectedArriveDate);

                                                        codRef.child(productId).setValue(map);

                                                        removeCardItem();

                                                        Intent home = new Intent(CODRentActivity.this , HomeActivity.class);
                                                        startActivity(home);

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            });

                        }

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

    private void removeCardItem() {
        FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(getUserId())
                .removeValue();
    }
}