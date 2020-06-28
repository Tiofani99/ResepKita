package com.developer_ngapak.resepkita.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.ViewPagerAdapter;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.fragment.description.AlatFragment;
import com.developer_ngapak.resepkita.ui.fragment.description.Deskripsi;
import com.developer_ngapak.resepkita.ui.fragment.description.ProsesFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "food";
    @BindView(R.id.iv_FotodetailMakanan)
    ImageView ivFoodDetail;
    @BindView(R.id.tv_item_detail)
    TextView tvDetail;
    @BindView(R.id.tabMode)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String foodName;
    private String foodIngredients;
    private String foodDesc;
    private String foodProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getData();
        setTittle(foodName);
        tvDetail.setText(foodDesc);
        setDetail();
    }

    private void setDetail() {
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
                        tvDetail.setText(foodDesc);
                        break;
                    case 1:
                        tabLayout.getTabAt(1);
                        tvDetail.setText(" ");
                        tvDetail.setText(foodIngredients);
                        break;
                    case 2:
                        tabLayout.getTabAt(2);
                        tvDetail.setText(" ");
                        tvDetail.setText(foodProcess);
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
        Food foodDetail = getIntent().getParcelableExtra(EXTRA_RECIPE);
        assert foodDetail != null;
        foodName = foodDetail.getName();
        foodDesc = foodDetail.getDetail();
        foodIngredients = foodDetail.getIngredient();
        foodProcess = foodDetail.getRecipe();
        String img = foodDetail.getImg();

        Glide.with(this)
                .load(img)
                .into(ivFoodDetail);
    }
}
