package com.developer_ngapak.resepkita.ui.fragment.home;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.rv_food_dashboard)
    RecyclerView rvFood;
    @BindView(R.id.recyclerCategory)
    RecyclerView rvCategory;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.swLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseRecyclerOptions<Food> options;
    private DatabaseReference databaseReference;
    private ArrayList<Category> foodLists = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        foodLists.clear();
        foodLists.addAll(getListData());
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
                intent.putExtra("search", query.toLowerCase());
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

        FirebaseRecyclerAdapter<Food, FireBaseViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FireBaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FireBaseViewHolder holder, final int position, @NonNull final Food model) {
                holder.tvTitle.setText(model.getName());
                Glide.with(holder.itemView.getContext())
                        .load(model.getImg())
                        .into(holder.img);

                holder.itemView.setOnClickListener(view -> {
                    Food food = getItem(position);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_RECIPE, food);
                    startActivity(intent);
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

    private void showCategory() {
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 4));
        CategoryAdapter adapter1 = new CategoryAdapter(foodLists);
        rvCategory.setAdapter(adapter1);
    }

    private void refreshData() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary1), getResources().getColor(R.color.colorPrimaryDark1));
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            showData();
        }, 2000)
        );
    }

    private ArrayList<Category> getListData() {
        String[] dataCategory = getResources().getStringArray(R.array.category_name);
        TypedArray dataPhoto = getResources().obtainTypedArray(R.array.category_img);

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