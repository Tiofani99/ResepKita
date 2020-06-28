package com.developer_ngapak.resepkita.ui.login;

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
import com.developer_ngapak.resepkita.ui.register.RegisterActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_email_login)
    TextInputEditText etEmail;
    @BindView(R.id.et_password_login)
    TextInputEditText etPassword;
    @BindView(R.id.btn_login_login)
    Button btnLogin;
    @BindView(R.id.tv_register_login)
    TextView tvRegister;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void login() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        String str_email = Objects.requireNonNull(etEmail.getText()).toString();
        String str_pw = Objects.requireNonNull(etPassword.getText()).toString();

        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pw)) {
            String message = getResources().getString(R.string.isi_full);
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(str_email, str_pw)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            String message = getResources().getString(R.string.email_pw_eror);
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_login:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_login_login:
                login();
                break;

        }
    }
}
