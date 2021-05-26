package za.co.robusttech.sewain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import za.co.robusttech.sewain.activities.AddCartActivity;


public class DeliveryAdressActivity extends AppCompatActivity {

    private EditText delivery_full_name , delivery_mobile_number , delivery_pincode , delivery_flat_house , delivery_area_colony , delivery_landmark , delivery_town_city ;
    private Button btn_add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_adress);

        delivery_full_name = findViewById(R.id.delivery_full_name);
        delivery_mobile_number = findViewById(R.id.delivery_mobile_number);
        delivery_pincode = findViewById(R.id.delivery_pincode);
        delivery_flat_house = findViewById(R.id.delivery_flat_house);
        delivery_area_colony = findViewById(R.id.delivery_area_colony);
        delivery_landmark = findViewById(R.id.delivery_landmark);
        delivery_town_city = findViewById(R.id.delivery_town_city);
        btn_add_address = findViewById(R.id.btn_add_address);

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_delivery_full_name = Objects.requireNonNull(delivery_full_name.getText()).toString();
                String txt_delivery_mobile_number = Objects.requireNonNull(delivery_mobile_number.getText()).toString();
                String txt_delivery_pincode = Objects.requireNonNull(delivery_pincode.getText()).toString();
                String txt_delivery_flat_house = Objects.requireNonNull(delivery_flat_house.getText()).toString();
                String txt_delivery_area_colony = Objects.requireNonNull(delivery_area_colony.getText()).toString();
                String txt_delivery_landmark = Objects.requireNonNull(delivery_landmark.getText()).toString();
                String txt_delivery_town_city = Objects.requireNonNull(delivery_town_city.getText()).toString();

                if (TextUtils.isEmpty(txt_delivery_full_name) || TextUtils.isEmpty(txt_delivery_mobile_number) || TextUtils.isEmpty(txt_delivery_pincode) || TextUtils.isEmpty(txt_delivery_flat_house)|| TextUtils.isEmpty(txt_delivery_area_colony)|| TextUtils.isEmpty(txt_delivery_landmark) || TextUtils.isEmpty(txt_delivery_town_city)){
                    Toast.makeText(DeliveryAdressActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                }  else {
                    address(txt_delivery_full_name, txt_delivery_mobile_number, txt_delivery_pincode , txt_delivery_flat_house ,txt_delivery_area_colony  ,txt_delivery_landmark , txt_delivery_town_city);
                }

            }

            private void address(String txt_delivery_full_name, String txt_delivery_mobile_number, String txt_delivery_pincode, String txt_delivery_flat_house, String txt_delivery_area_colony, String txt_delivery_landmark, String txt_delivery_town_city) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                String userid = firebaseUser.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Address");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("fullName", txt_delivery_full_name);
                hashMap.put("mobileNumber", txt_delivery_mobile_number);
                hashMap.put("pinCode", txt_delivery_pincode);
                hashMap.put("flatHouse", txt_delivery_flat_house);
                hashMap.put("areaColony", txt_delivery_area_colony);
                hashMap.put("landMark", txt_delivery_landmark);
                hashMap.put("townCity", txt_delivery_town_city);

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(DeliveryAdressActivity.this, AddCartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


            }
        });



    }
}