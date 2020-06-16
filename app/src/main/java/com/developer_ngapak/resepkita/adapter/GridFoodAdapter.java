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

public class GridFoodAdapter extends RecyclerView.Adapter<GridFoodAdapter.GridViewHolder> {

    private ArrayList<Food> listFood;
    private Context mContext;

    public GridFoodAdapter(ArrayList<Food> list){
        this.listFood = list;
    }

    public GridFoodAdapter(Context mContext, ArrayList<Food> listFood){
        this.mContext = mContext;
        this.listFood = listFood;
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

        Glide.with(holder.itemView.getContext())
                .load(listFood.get(position).getImg())
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);
        holder.nameFood.setText(food.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_NAME,food.getName());
                intent.putExtra(DetailActivity.EXTRA_IMAGE,food.getImg());
                intent.putExtra(DetailActivity.EXTRA_PROCESS,food.getRecipe());
                intent.putExtra(DetailActivity.EXTRA_DESC,food.getDetail());
                intent.putExtra(DetailActivity.EXTRA_ALAT,food.getIngredient());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView nameFood;


        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            nameFood = itemView.findViewById(R.id.tv_item_name);
        }
    }
}