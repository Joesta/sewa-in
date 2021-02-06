package za.co.robusttech.sewa_in.activities;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.jar.Attributes;

import za.co.robusttech.sewa_in.R;

public class UploadDataActivity extends AppCompatActivity  {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "MainAct";
    String miUrlOk = "";

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText   productAvailability , productCategory ,productDeliveryTime , productDesciption , productDiscount , productName , productOriginalPrice , productPrice , productRatings;
    private ImageView mImageView;
    String name;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, mDatabaseRef1;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dat);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mImageView = findViewById(R.id.image_view);

        productAvailability = findViewById(R.id.productAvailability);
        productCategory = findViewById(R.id.productCategory);
        productDeliveryTime = findViewById(R.id.productDeliveryTime);
        productDesciption = findViewById(R.id.productDesciption);
        productDiscount = findViewById(R.id.productDiscount);
        productName = findViewById(R.id.productName);
        productOriginalPrice = findViewById(R.id.productOriginalPrice);
        productPrice = findViewById(R.id.productPrice);
        productRatings = findViewById(R.id.productRatings);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadDataActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
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

    private void uploadFile() {
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");


                        String productId = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productId", productId);
                        hashMap.put("productImage", miUrlOk);
                        hashMap.put("productCategory", productCategory.getText().toString());
                        hashMap.put("productAvailability", productAvailability.getText().toString());
                        hashMap.put("productDeliveryTime", productDeliveryTime.getText().toString());
                        hashMap.put("productDesciption", productDesciption.getText().toString());
                        hashMap.put("productDiscount", productDiscount.getText().toString());
                        hashMap.put("productName", productName.getText().toString());
                        hashMap.put("productOriginalPrice", productOriginalPrice.getText().toString());
                        hashMap.put("productPrice", productPrice.getText().toString());
                        hashMap.put("productRatings", productRatings.getText().toString());


                        reference.child(productId).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(UploadDataActivity.this, UploadDataActivity.class));
                        finish();

                    } else {
                        Toast.makeText(UploadDataActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(UploadDataActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }


    }}





