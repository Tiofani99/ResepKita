package com.developer_ngapak.resepkita.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer_ngapak.resepkita.DashboardActivity;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etUserName, etFullName, etEmail, etPassword;
    Button btnRegister;
    TextView tvLogin;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = findViewById(R.id.et_username_register);
        etFullName = findViewById(R.id.et_fullname_register);
        etEmail = findViewById(R.id.et_email_register);
        etPassword = findViewById(R.id.et_password_register);
        btnRegister = findViewById(R.id.btn_register_register);
        tvLogin = findViewById(R.id.tv_login_register);

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    private void checkRegister() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        String str_username = Objects.requireNonNull(etUserName.getText()).toString();
        String str_fullname = Objects.requireNonNull(etFullName.getText()).toString();
        String str_email = Objects.requireNonNull(etEmail.getText()).toString();
        String str_password = Objects.requireNonNull(etPassword.getText()).toString();

        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email)
                || TextUtils.isEmpty(str_password)) {
            String message = getResources().getString(R.string.isi_full);
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        } else if (str_password.length() < 6) {
            String message = getResources().getString(R.string.pw_min_6);
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        } else {
            register(str_username, str_fullname, str_email, str_password);
        }

    }

    private void register(final String username, final String full, String email, String pw) {
        auth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String uid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", uid);
                            hashMap.put("username", username);
                            hashMap.put("fullname", full);
                            hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/sambatan-kita.appspot.com/o/User_24px.png?alt=media&token=334164d4-0dc9-4e49-8b71-4eb7c29850cb");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            String message = getResources().getString(R.string.email_error);
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.tv_login_register:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_register_register:
                checkRegister();
                break;
        }

    }
}
