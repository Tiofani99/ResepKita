package com.developer_ngapak.resepkita.ui.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer_ngapak.resepkita.MainActivity;
import com.developer_ngapak.resepkita.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_textResepku)
    TextView tvText;
    @BindView(R.id.cv_circle)
    CircleImageView cvWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSplash();
    }

    public void setSplash() {

        final Intent intent = new Intent(this, MainActivity.class);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim);
        ivLogo.startAnimation(animation);
        tvText.startAnimation(animation);
        cvWhite.startAnimation(animation);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        thread.start();
    }
}
