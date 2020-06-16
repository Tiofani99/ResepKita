package com.developer_ngapak.resepkita.ui;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer_ngapak.resepkita.MainActivity;
import com.developer_ngapak.resepkita.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvText;
    private CircleImageView cvWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ivLogo = findViewById(R.id.iv_logo);
        tvText = findViewById(R.id.tv_textResepku);
        cvWhite = findViewById(R.id.cv_circle);
        setSplash();
    }

    public void setSplash(){

        final Intent intent = new Intent(this, MainActivity.class);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
        ivLogo.startAnimation(animation);
        tvText.startAnimation(animation);
        cvWhite.startAnimation(animation);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        thread.start();
    }
}
