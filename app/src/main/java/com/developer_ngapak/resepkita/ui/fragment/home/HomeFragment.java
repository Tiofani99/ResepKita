package com.developer_ngapak.resepkita.ui.fragment.home;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.CategoryAdapter;
import com.developer_ngapak.resepkita.adapter.FireBaseViewHolder;
import com.developer_ngapak.resepkita.entity.Category;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.detail.DetailActivity;
import com.developer_ngapak.resepkita.ui.search.ShowDataActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView rvFood,rvCategory;
    private ArrayList<Food> list = new ArrayList<>();
    private FirebaseRecyclerOptions<Food> options;
    private FirebaseRecyclerAdapter<Food, FireBaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private ArrayList<Category> listt = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TypedArray dataPhoto;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvFood = root.findViewById(R.id.rv_food_dashboard);
        rvCategory = root.findViewById(R.id.recyclerCategory);
        searchView = root.findViewById(R.id.searchView);
        swipeRefreshLayout = root.findViewById(R.id.swLayout);

        listt.clear();
        listt.addAll(getListData());
        rvCategory.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");
        databaseReference.keepSynced(true);
        showData();
        showCategory();
        refreshData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getContext(), ShowDataActivity.class);
                intent.putExtra("search",query.toLowerCase());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

    private void showData() {
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(databaseReference, Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FireBaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FireBaseViewHolder holder, final int position, @NonNull final Food model) {
                holder.tvTitle.setText(model.getName());
                Glide.with(holder.itemView.getContext())
                        .load(model.getImg())
                        .into(holder.img);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String cName = getItem(position).getName();
                        final String cDetail = getItem(position).getDetail();
                        final String cCategory = getItem(position).getCategory();
                        final String cIngredient = getItem(position).getIngredient();
                        final String cRecipe = getItem(position).getRecipe();
                        final String cImage = getItem(position).getImg();

                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_NAME, cName);
                        intent.putExtra(DetailActivity.EXTRA_DESC, cDetail);
                        intent.putExtra(DetailActivity.EXTRA_ALAT, cIngredient);
                        intent.putExtra(DetailActivity.EXTRA_PROCESS, cRecipe);
                        intent.putExtra(DetailActivity.EXTRA_IMAGE, cImage);

                        startActivity(intent);


                    }
                });

            }

            @NonNull
            @Override
            public FireBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FireBaseViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.list_grid_makanan, parent, false));
            }
        };

        rvFood.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvFood.setAdapter(adapter);
        adapter.startListening();
    }

    private void showCategory(){
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(),4));
        CategoryAdapter adapter1 = new CategoryAdapter(listt);
        rvCategory.setAdapter(adapter1);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (adapter != null) {
//            adapter.startListening();
//        }
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(backPressedTime + 2000 > System.currentTimeMillis() ){
//            super.onBackPressed();
//            //onDestroy();
//            return;
//        }else{
//            Toast.makeText(this,"Tekan sekali lagi untuk keluar",Toast.LENGTH_SHORT).show();
//        }
//        backPressedTime = System.currentTimeMillis();
//    }

    private void refreshData() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary1), getResources().getColor(R.color.colorPrimaryDark1));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                swipeRefreshLayout.setRefreshing(false);
                                                                showData();
                                                            }
                                                        }, 2000);
                                                    }
                                                }
        );
    }

    private ArrayList<Category> getListData() {
        String[] dataCategory = getResources().getStringArray(R.array.category_name);
        dataPhoto = getResources().obtainTypedArray(R.array.category_img);

        ArrayList<Category> listData = new ArrayList<>();
        for (int i = 0; i < dataCategory.length; i++) {
            Category category = new Category();
            category.setName(dataCategory[i]);
            category.setImg(dataPhoto.getResourceId(i, -1));

            listData.add(category);

        }

        return listData;

    }


}