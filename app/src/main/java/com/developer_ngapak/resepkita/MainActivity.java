package com.developer_ngapak.resepkita;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developer_ngapak.resepkita.ui.login.LoginActivity;
import com.developer_ngapak.resepkita.ui.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_login_start)
    Button btnLogin;
    @BindView(R.id.btn_register_start)
    Button btnRegister;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login_start:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;

            case R.id.btn_register_start:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;
        }

    }
}
