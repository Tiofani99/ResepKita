package com.developer_ngapak.resepkita.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etName, etDetail, etIngredient, etRecipe;
    ImageView imgPhoto;
    Button btnUpload;
    TextView tvTitle,tvCategory;

    String mStoragePath = "All_Images/";
    String mDatabasePath = "Data_by_User";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Uri filePathUri;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    ProgressDialog progressDialog;


    int IMAGE_REQUEST_CODE = 5;
    String cTitle, cDetail, cCategory, cIngredient, cRecipe, cImage, cSearch;

    String uploading ;
    String success ;
    String failed ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        etName = findViewById(R.id.et_name);
        tvCategory = findViewById(R.id.tv_category);
        etDetail = findViewById(R.id.et_detail);
        etIngredient = findViewById(R.id.et_ingredient);
        etRecipe = findViewById(R.id.et_recipe);
        imgPhoto = findViewById(R.id.iv_img_upload);
        btnUpload = findViewById(R.id.btn_upload);
        tvTitle = findViewById(R.id.tv_title);


        uploading = getResources().getString(R.string.uploading);
        success = getResources().getString(R.string.upload_succes);
        failed = getResources().getString(R.string.failed);

        String mesg = getResources().getString(R.string.add_recipe);
        tvTitle.setText(mesg);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        imgPhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //Buat penyimpanan foto
        mStorageReference = getInstance().getReference();

        //Lokasi buat databasenya
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);

        progressDialog = new ProgressDialog(AddRecipeActivity.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            cTitle = intent.getString("cTitle");
            cDetail = intent.getString("cDetail");
            cCategory = intent.getString("cCategory");
            cIngredient = intent.getString("cIngredient");
            cRecipe = intent.getString("cRecipe");
            cImage = intent.getString("cImage");

            etName.setText(cTitle);
            etDetail.setText(cDetail);
            tvCategory.setText(cCategory);
            etIngredient.setText(cIngredient);
            etRecipe.setText(cRecipe);

            Glide.with(AddRecipeActivity.this)
                    .load(cImage)
                    .into(imgPhoto);

            String msg = getResources().getString(R.string.update);
            String msgg = getResources().getString(R.string.update_recipe);

            tvTitle.setText(msgg);
            btnUpload.setText(msg);
        }

        Spinner spinner = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this,R.array.category_name,android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = adapterView.getItemAtPosition(i).toString();
                tvCategory.setText(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void uploadImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select Image"), IMAGE_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img_upload:
                uploadImg();
                break;

            case R.id.btn_upload:
                String msg = getResources().getString(R.string.upload).toLowerCase();
                if (btnUpload.getText().toString().toLowerCase().equals(msg)) {
                    uploadDataToFirebase();
                } else {
                    updateDataToFirebase();
                }
                break;
        }
    }


    private void updateDataToFirebase() {
        String msg = getResources().getString(R.string.please_wait);
        progressDialog.setMessage(msg);
        progressDialog.show();
        Toast.makeText(this, cImage, Toast.LENGTH_SHORT).show();
        deletePreviousImage();
    }

    private void deletePreviousImage() {
        StorageReference storageReference = getInstance().getReferenceFromUrl(cImage);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadNewImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AddRecipeActivity.this, "gagal apus gambar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void uploadNewImage() {
        if (filePathUri != null) {
            String msg = getResources().getString(R.string.uploading);
            progressDialog.setTitle(msg);
            progressDialog.show();

            StorageReference storageReference2 = mStorageReference.child(mStoragePath + System.currentTimeMillis() + "." + getFileExtension(filePathUri));
            Bitmap bitmap = ((BitmapDrawable) imgPhoto.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //Buat kompress gambar gaes
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = storageReference2.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String success = getResources().getString(R.string.upload_succes);
                    Toast.makeText(AddRecipeActivity.this, success, Toast.LENGTH_SHORT).show();
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri uri = uriTask.getResult();
                    assert uri != null;
                    String file = uri.toString();
                    updateDatabase(file);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    String failed = getResources().getString(R.string.failed);
                    Toast.makeText(AddRecipeActivity.this, failed, Toast.LENGTH_SHORT).show();

                    //progressDialog.dismiss();
                }
            });
        }

    }

    private void updateDatabase(final String s) {
        final String name = etName.getText().toString();
        final String category = tvCategory.getText().toString();
        final String detail = etDetail.getText().toString();
        final String ingredient = etIngredient.getText().toString();
        final String recipe = etRecipe.getText().toString();
        final String search = etName.getText().toString().toLowerCase();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("Data_by_User");


        Query query = mRef.orderByChild("name").equalTo(cTitle);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("category").setValue(category);
                    ds.getRef().child("detail").setValue(detail);
                    ds.getRef().child("ingredient").setValue(ingredient);
                    ds.getRef().child("name").setValue(name);
                    ds.getRef().child("recipe").setValue(recipe);
                    ds.getRef().child("img").setValue(s);
                    ds.getRef().child("search").setValue(search);
                }
                progressDialog.dismiss();
                Toast.makeText(AddRecipeActivity.this, success, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddRecipeActivity.this, failed, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void uploadDataToFirebase() {
        Boolean cek = cekForm();
        if (cek) {
            if (filePathUri != null) {
                progressDialog.setTitle(uploading);
                progressDialog.show();

                StorageReference storageReference2nd = mStorageReference.child(mStoragePath + System.currentTimeMillis() + "." + getFileExtension(filePathUri));

                storageReference2nd.putFile(filePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();


                                String mName = etName.getText().toString().trim();
                                String mCategory = tvCategory.getText().toString().trim();
                                String mDetail = etDetail.getText().toString().trim();
                                String mIngredient = etIngredient.getText().toString().trim();
                                String mRecipe = etRecipe.getText().toString().trim();
                                String mSearch = etName.getText().toString().trim().toLowerCase();

                                progressDialog.dismiss();
                                String id = user.getUid();


                                Toast.makeText(AddRecipeActivity.this, success, Toast.LENGTH_SHORT).show();
                                Food food = new Food(mName, mCategory, mDetail, mIngredient, mRecipe, downloadUrl.toString(), mSearch,id);

                                String imgUploadId = mDatabaseReference.push().getKey();

                                mDatabaseReference.child(imgUploadId).setValue(food);
                                finish();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.setTitle(uploading);
                            }
                        });
            } else {
                Toast.makeText(AddRecipeActivity.this, failed, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getFileExtension(Uri filePathUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(filePathUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                imgPhoto.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private Boolean cekForm() {
        String eName = etName.getText().toString();
        String eDetail = etDetail.getText().toString();
        String eIngredient = etIngredient.getText().toString();
        String eRecipe = etRecipe.getText().toString();
        boolean cek = false;

        String msgErrorName = getResources().getString(R.string.data_required);
        if (eName.isEmpty()) {
            etName.setError(msgErrorName);
        } else if (eDetail.isEmpty()) {
            etDetail.setError(msgErrorName);
        } else if (eIngredient.isEmpty()) {
            etIngredient.setError(msgErrorName);
        } else if (eRecipe.isEmpty()) {
            etRecipe.setError(msgErrorName);
        } else {
            cek = true;
        }
        return cek;
    }
}
