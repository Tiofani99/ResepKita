package com.developer_ngapak.resepkita.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.MainActivity;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.model.Category;
import com.developer_ngapak.resepkita.ui.ShowDataActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.GridViewHolder> {

    ArrayList<Category> list;

    public CategoryAdapter(ArrayList<Category> list){
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_category,parent,false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.GridViewHolder holder, int position) {
        final Category category = list.get(position);
        holder.title.setText(category.getName());
        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImg())
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = category.getName();
                Intent intent = new Intent(holder.itemView.getContext(), ShowDataActivity.class);
                intent.putExtra("category",c);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.categoryName);
            img = itemView.findViewById(R.id.categoryThumb);
        }
    }
}
