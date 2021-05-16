package za.co.robusttech.sewain.activities;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import za.co.robusttech.sewain.R;
import za.co.robusttech.sewain.constants.IProductConstants;

public class UploadDataActivity extends AppCompatActivity  {

    private static final int PICK_IMAGE_REQUEST = 1;
    String miUrlOk = "";

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText   productCategory  , productDesciption , productDiscount , productName  , productPrice , productRatings , productQuantity;
    private ImageView mImageView;

    private Uri mImageUri;

    private StorageReference mStorageRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dat);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mImageView = findViewById(R.id.image_view);

        productQuantity = findViewById(R.id.productQuantity);
        productCategory = findViewById(R.id.productCategory);
        productDesciption = findViewById(R.id.productDescription);
        productDiscount = findViewById(R.id.productDiscount);
        productName = findViewById(R.id.productName);
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

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(IProductConstants.PRODUCT_ID, productId);
                        map.put(IProductConstants.PRODUCT_IMAGE_URL, miUrlOk);
                        map.put(IProductConstants.PRODUCT_CATEGORY, productCategory.getText().toString());
                        map.put(IProductConstants.PRODUCT_DESCRIPTION, productDesciption.getText().toString());
                        map.put(IProductConstants.PRODUCT_DISCOUNT, productDiscount.getText().toString());
                        map.put(IProductConstants.PRODUCT_NAME, productName.getText().toString());
                        map.put(IProductConstants.PRODUCT_PRICE, productPrice.getText().toString());
                        map.put(IProductConstants.PRODUCT_RATING, productRatings.getText().toString());
                        map.put(IProductConstants.PRODUCT_QUANTITY, productQuantity.getText().toString());

                        reference.child(productId).setValue(map);

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





