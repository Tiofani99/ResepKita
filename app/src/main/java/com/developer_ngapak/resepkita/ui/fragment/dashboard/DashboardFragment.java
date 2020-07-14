package com.developer_ngapak.resepkita.ui.fragment.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.FireBaseViewHolder;
import com.developer_ngapak.resepkita.adapter.GridFoodAdapter;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.add_recipe.AddRecipeActivity;
import com.developer_ngapak.resepkita.ui.detail.DetailActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardFragment extends Fragment {

    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.rvFood)
    RecyclerView rvFood;
    @BindView(R.id.swLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference databaseReference;


    private FirebaseRecyclerOptions<Food> options;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,root);
        FloatingActionButton btnAdd = root.findViewById(R.id.fab);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            startActivity(intent);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data_by_User");
        databaseReference.keepSynced(true);
        showData();
        refreshData();

        SearchView searchView = root.findViewById(R.id.search_dashboard);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchData(newText);
                return false;
            }
        });

        return root;
    }


    private void showData() {
        showLoading(true);
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
                return new FireBaseViewHolder(LayoutInflater.from(getActivity())
                        .inflate(R.layout.list_grid_makanan, parent, false));
            }
        };

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        rvFood.setLayoutManager(gridLayoutManager);
        rvFood.setAdapter(adapter);
        adapter.startListening();
        showLoading(false);

    }

    private void showLoading(Boolean state) {
        if (state) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }


    private void refreshData() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent2), getResources().getColor(R.color.colorPrimary2));
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                    showData();
                }, 3000)
        );
    }

    private void searchData(String str) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Data_by_User");
        mRef.orderByChild("name")
                .startAt(str)
                .endAt(str + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Food> myList = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                myList.add(ds.getValue(Food.class));
                            }
                        }
                        GridFoodAdapter cardViewAdapter = new GridFoodAdapter(myList);
                        rvFood.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        rvFood.setAdapter(cardViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }
}