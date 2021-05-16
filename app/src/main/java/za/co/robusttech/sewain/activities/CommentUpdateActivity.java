package za.co.robusttech.sewain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.constants.IProductConstants;

public class CommentUpdateActivity extends AppCompatActivity {

    TextView title_txt, review_txt;
    Button update_review;
    String productId;
    ImageView mImageView;
    Uri mImageUri;
    StorageReference mStorageRef;
    StorageTask mUploadTask;
    RatingBar rating_bar_review;

    private static final int PICK_IMAGE_REQUEST = 1;
    String miUrlOk = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_update);

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");


        title_txt = findViewById(R.id.add_comment_title);
        review_txt = findViewById(R.id.add_comment_c);
        update_review = findViewById(R.id.btn_update_review);
        mImageView = findViewById(R.id.image_view_review);
        rating_bar_review = findViewById(R.id.rating_bar_review);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(mImageView.getContext()).load(mImageUri).into(mImageView);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addComment(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if (mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri);
            mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        miUrlOk = downloadUri.toString();

                        pd.dismiss();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(productId);

                        String commentid = reference.push().getKey();

                        float rating = rating_bar_review.getRating();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("comment", review_txt.getText().toString());
                        hashMap.put("commentTitle", title_txt.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("commentid", commentid);
                        hashMap.put("commentRatings", rating);
                        hashMap.put(IProductConstants.PRODUCT_IMAGE_URL, miUrlOk);

                        reference.child(commentid).setValue(hashMap);

                        Intent intent = new Intent(CommentUpdateActivity.this , HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(CommentUpdateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CommentUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(CommentUpdateActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }


}