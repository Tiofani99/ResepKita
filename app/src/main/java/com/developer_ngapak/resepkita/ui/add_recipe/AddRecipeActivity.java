package com.developer_ngapak.resepkita.ui.add_recipe;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
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
import com.developer_ngapak.resepkita.entity.Food;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_RECIPE = "food";
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;

    private String mStoragePath = "All_Images/";
    private Uri filePathUri;

    ProgressDialog progressDialog;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_detail)
    EditText etDetail;
    @BindView(R.id.et_ingredient)
    EditText etIngredient;
    @BindView(R.id.et_recipe)
    EditText etRecipe;
    @BindView(R.id.iv_img_upload)
    ImageView imgPhoto;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.spinner_category)
    Spinner spinner;


    int IMAGE_REQUEST_CODE = 5;
    String cTitle;
    String cImage;

    String uploading;
    String success;
    String failed;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        ButterKnife.bind(this);


        uploading = getResources().getString(R.string.uploading);
        success = getResources().getString(R.string.upload_succes);
        failed = getResources().getString(R.string.failed);


        String msgAddRecipe = getResources().getString(R.string.add_recipe);
        tvTitle.setText(msgAddRecipe);
        setTitle(msgAddRecipe);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        imgPhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //Buat penyimpanan foto
        mStorageReference = getInstance().getReference();

        //Lokasi buat databasenya
        String mDatabasePath = "Data_by_User";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);

        progressDialog = new ProgressDialog(AddRecipeActivity.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            Food food = intent.getParcelable(EXTRA_RECIPE);
            assert food != null;
            cTitle = food.getName();
            cImage = food.getImg();
            etName.setText(food.getName());
            etDetail.setText(food.getDetail());
            etIngredient.setText(food.getIngredient());
            etRecipe.setText(food.getRecipe());
            Log.d("Coba","Link foto "+food.getImg());

            Glide.with(AddRecipeActivity.this)
                    .load(food.getImg())
                    .into(imgPhoto);

            String msgStatusUpdate = getResources().getString(R.string.update);
            String msgUpdateRecipe = getResources().getString(R.string.update_recipe);

            tvTitle.setText(msgUpdateRecipe);
            setTitle(msgUpdateRecipe);
            btnUpload.setText(msgStatusUpdate);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                Log.d("Coba","Kategori : "+category);
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
        Log.d("Coba","Link Image "+storageReference);
        storageReference.delete().addOnSuccessListener(aVoid -> uploadNewImage()).addOnFailureListener(e -> {
            Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(AddRecipeActivity.this, "gagal Hapus gambar", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                String success = getResources().getString(R.string.upload_succes);
                Toast.makeText(AddRecipeActivity.this, success, Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri uri = uriTask.getResult();
                assert uri != null;
                String file = uri.toString();
                Log.d("Coba","Link Image baru "+file);
                updateDatabase(file);

            }).addOnFailureListener(e -> {
                Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                String failed = getResources().getString(R.string.failed);
                Toast.makeText(AddRecipeActivity.this, failed, Toast.LENGTH_SHORT).show();

                //progressDialog.dismiss();
            });
        }

    }

    private void updateDatabase(final String s) {
        final String name = etName.getText().toString();
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
                        .addOnSuccessListener(taskSnapshot -> {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();


                            String mName = etName.getText().toString().trim();
                            String mCategory = category;
                            String mDetail = etDetail.getText().toString().trim();
                            String mIngredient = etIngredient.getText().toString().trim();
                            String mRecipe = etRecipe.getText().toString().trim();
                            String mSearch = etName.getText().toString().trim().toLowerCase();

                            progressDialog.dismiss();
                            String id = user.getUid();


                            Toast.makeText(AddRecipeActivity.this, success, Toast.LENGTH_SHORT).show();
                            Food food = new Food(mName, mCategory, mDetail, mIngredient, mRecipe, downloadUrl.toString(), mSearch, id);

                            String imgUploadId = mDatabaseReference.push().getKey();

                            mDatabaseReference.child(imgUploadId).setValue(food);
                            finish();


                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setTitle(String title){
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home ){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
