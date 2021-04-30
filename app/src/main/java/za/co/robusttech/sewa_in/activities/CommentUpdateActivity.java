package za.co.robusttech.sewa_in.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import za.co.robusttech.sewa_in.R;

public class CommentUpdateActivity extends AppCompatActivity {

    TextView title_txt, review_txt;
    Button update_review;
    String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_update);

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");


        title_txt = findViewById(R.id.add_comment_title);
        review_txt = findViewById(R.id.add_comment_c);
        update_review = findViewById(R.id.btn_update_review);

        update_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title_txt.getText().toString().equals("") || review_txt.getText().toString().equals("")){
                    Toast.makeText(CommentUpdateActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

    }

    private void addComment(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(productId);

        String commentid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", review_txt.getText().toString());
        hashMap.put("commentTitle", title_txt.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("commentid", commentid);

        reference.child(commentid).setValue(hashMap);

        Intent intent = new Intent(CommentUpdateActivity.this , HomeActivity.class);
        startActivity(intent);

    }


}