package com.developer_ngapak.resepkita.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.FireBaseViewHolder;
import com.developer_ngapak.resepkita.adapter.GridFoodAdapter;
import com.developer_ngapak.resepkita.entity.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity {

    private RecyclerView rvFood;
    private ArrayList<Food> list = new ArrayList<>();
    private FirebaseRecyclerOptions<Food> options;
    private FirebaseRecyclerAdapter<Food, FireBaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        rvFood = findViewById(R.id.rv_food);
        tvNoData = findViewById(R.id.tv_noData);
        //rvFood.setHasFixedSize(true);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");
        databaseReference.keepSynced(true);
        //showData();
        String category = getIntent().getStringExtra("category");
        String search = getIntent().getStringExtra("search");

        if(category != null){
            setActionBarTitle("Kategori "+category);
            showCategory(category);
        }else{
            setActionBarTitle("'"+search+"'");
            searchData(search);
        }
    }

    private void setActionBarTitle(String title){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }


    private void searchData(String str) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Data");
        mRef.orderByChild("search")
                .startAt(str)
                .endAt(str+"\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Food> myList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            tvNoData.setText(null);
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                myList.add(ds.getValue(Food.class));
                            }
                            GridFoodAdapter gridFoodAdapter = new GridFoodAdapter(myList);
                            rvFood.setLayoutManager(new GridLayoutManager(ShowDataActivity.this, 2));
                            rvFood.setAdapter(gridFoodAdapter);
                        }else{
                            myList.clear();
                            GridFoodAdapter gridFoodAdapter = new GridFoodAdapter(myList);
                            rvFood.setLayoutManager(new GridLayoutManager(ShowDataActivity.this, 2));
                            rvFood.setAdapter(gridFoodAdapter);
                            String msg = getResources().getString(R.string.no_recipe_found);
                            tvNoData.setText(msg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showCategory(String str) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Data");
        mRef.orderByChild("category")
                .startAt(str)
                .endAt(str + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Food> myList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            tvNoData.setText(null);
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                myList.add(ds.getValue(Food.class));
                            }

                            GridFoodAdapter gridFoodAdapter = new GridFoodAdapter(list);
                            rvFood.setAdapter(gridFoodAdapter);
                        }else{
                            String msg = getResources().getString(R.string.no_recipe_found);
                            tvNoData.setText(msg);
                        }
                        GridFoodAdapter gridFoodAdapter = new GridFoodAdapter(myList);
                        rvFood.setLayoutManager(new GridLayoutManager(ShowDataActivity.this, 2));
                        rvFood.setAdapter(gridFoodAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Nama makanan");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
                    searchData(query);
                    setActionBarTitle("'"+query+"'");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //searchData(newText);
                    return false;
                }
            });
        } else {
            //showData();
        }


        return true;
    }
}
