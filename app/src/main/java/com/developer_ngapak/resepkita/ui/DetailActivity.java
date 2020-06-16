package com.developer_ngapak.resepkita.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.ViewPagerAdapter;
import com.developer_ngapak.resepkita.model.Food;
import com.developer_ngapak.resepkita.ui.fragment.description.AlatFragment;
import com.developer_ngapak.resepkita.ui.fragment.description.Deskripsi;
import com.developer_ngapak.resepkita.ui.fragment.description.ProsesFragment;
import com.google.android.material.tabs.TabLayout;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivDetailMakanan;
    private TextView tvDetail;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private String tittle;
    public static final String EXTRA_NAME = "nama makanan";
    public static final String EXTRA_ALAT = "alat dan bahan";
    public static final String EXTRA_DESC = "deskripsi makanan";
    public static final String EXTRA_PROCESS = "resepnya";
    public static final String EXTRA_IMAGE = "0";

    private String nama, alat, desc, proses,img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetail = findViewById(R.id.tv_item_detail);
        ivDetailMakanan = findViewById(R.id.iv_FotodetailMakanan);
        tabLayout = findViewById(R.id.tabMode);
        viewPager = findViewById(R.id.viewPager);

        getData();
        setTittle(nama);
        tvDetail.setText(desc);


        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Menambahkan fragment
        adapter.addFragment(new Deskripsi(), "Deskripsi");
        adapter.addFragment(new AlatFragment(), "Alat dan bahan");
        adapter.addFragment(new ProsesFragment(), "Cara Membuat");

        viewPager.setAdapter(adapter);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0);
                        tvDetail.setText(" ");
                        tvDetail.setText(desc);
                        break;
                    case 1:
                        tabLayout.getTabAt(1);
                        tvDetail.setText(" ");
                        tvDetail.setText(alat);
                        break;
                    case 2:
                        tabLayout.getTabAt(2);
                        tvDetail.setText(" ");
                        tvDetail.setText(proses);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTittle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }




    private void getData() {
        nama = getIntent().getStringExtra(EXTRA_NAME);
        desc = getIntent().getStringExtra(EXTRA_DESC);
        alat = getIntent().getStringExtra(EXTRA_ALAT);
        proses = getIntent().getStringExtra(EXTRA_PROCESS);
        img = getIntent().getStringExtra(EXTRA_IMAGE);

        Glide.with(this)
                .load(img)
                .into(ivDetailMakanan);
    }
}
