package com.developer_ngapak.resepkita.ui.about;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer_ngapak.resepkita.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {
    @BindView(R.id.rv_developer)
    RecyclerView rvDeveloper;

    private String[] name;
    private TypedArray photo;
    private String[] nim;
    private String[] address;
    private String[] hobbies;
    private String[] quotes;
    private String[] githubName;
    private String[] igName;
    private ArrayList<Developer> developers;


    private DeveloperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initAdapter();
        initTitle();
        getData();
        setData();
    }

    private void initAdapter() {
        rvDeveloper.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DeveloperAdapter();
        rvDeveloper.setAdapter(adapter);
    }

    private void getData() {
        name = getResources().getStringArray(R.array.developer_name);
        photo = getResources().obtainTypedArray(R.array.developer_photos);
        nim = getResources().getStringArray(R.array.developer_nim);
        address = getResources().getStringArray(R.array.developer_address);
        hobbies = getResources().getStringArray(R.array.developer_hobbies);
        quotes = getResources().getStringArray(R.array.developer_quotes);
        githubName = getResources().getStringArray(R.array.developer_github);
        igName = getResources().getStringArray(R.array.developer_ig);
    }

    private void setData(){
        developers = new ArrayList<>();
        for(int i = 0 ; i<name.length ; i++){
            Developer developer = new Developer();
            developer.setName(name[i]);
            developer.setPhoto(photo.getResourceId(i,-1));
            developer.setNim(nim[i]);
            developer.setAddress(address[i]);
            developer.setHobbies(hobbies[i]);
            developer.setQuotes(quotes[i]);
            developer.setGithubName(githubName[i]);
            developer.setIgName(igName[i]);
            developers.add(developer);
        }
        adapter.setData(developers);
    }

    private void initTitle() {
        if(getSupportActionBar()!= null){
            getSupportActionBar().setTitle(getResources().getString(R.string.project_id));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}