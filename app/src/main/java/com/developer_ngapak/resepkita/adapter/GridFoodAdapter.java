package com.developer_ngapak.resepkita.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.detail.DetailActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GridFoodAdapter extends RecyclerView.Adapter<GridFoodAdapter.GridViewHolder> {

    private ArrayList<Food> listFood;

    public GridFoodAdapter(ArrayList<Food> list) {
        this.listFood = list;
    }

    @NonNull
    @Override
    public GridFoodAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grid_makanan, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridFoodAdapter.GridViewHolder holder, int position) {
        final Food food = listFood.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_item_photo)
        ImageView imgPhoto;
        @BindView(R.id.tv_item_name)
        TextView nameFood;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Food food) {
            Glide.with(itemView.getContext())
                    .load(food.getImg())
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
            nameFood.setText(food.getName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Food food = listFood.get(position);
            Context context = view.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_RECIPE, food);
            context.startActivity(intent);
        }
    }
}