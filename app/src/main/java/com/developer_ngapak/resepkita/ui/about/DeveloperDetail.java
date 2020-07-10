package com.developer_ngapak.resepkita.ui.about;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeveloperDetail extends AppCompatActivity {
    public static final String EXTRA_DETAIL_DEVELOPER = "extra";
    @BindView(R.id.civ_developer_photo)
    CircleImageView civDeveloperPhoto;
    @BindView(R.id.tv_developer_name)
    TextView tvDeveloperName;
    @BindView(R.id.tv_developer_nim)
    TextView tvDeveloperNim;
    @BindView(R.id.tv_developer_github)
    TextView tvDeveloperGithub;
    @BindView(R.id.tv_developer_ig)
    TextView tvDeveloperIg;
    @BindView(R.id.tv_developer_address)
    TextView tvDeveloperAddress;
    @BindView(R.id.tv_developer_hobby)
    TextView tvDeveloperHobby;
    @BindView(R.id.tv_developer_quotes)
    TextView tvDeveloperQuotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_detail);
        ButterKnife.bind(this);
        setData();

    }

    private void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setData() {
        Developer developer = getIntent().getParcelableExtra(EXTRA_DETAIL_DEVELOPER);
        assert developer != null;
        tvDeveloperName.setText(developer.getName());
        tvDeveloperNim.setText(developer.getNim());
        tvDeveloperGithub.setText(developer.getGithubName());
        tvDeveloperIg.setText(developer.getIgName());
        tvDeveloperAddress.setText(developer.getAddress());
        tvDeveloperHobby.setText(developer.getHobbies());
        tvDeveloperQuotes.setText(developer.getQuotes());

        Glide.with(this)
                .load(developer.getPhoto())
                .into(civDeveloperPhoto);

        setTitle(developer.getName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}