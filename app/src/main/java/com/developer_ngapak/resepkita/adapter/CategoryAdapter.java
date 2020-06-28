package com.developer_ngapak.resepkita.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.entity.Category;
import com.developer_ngapak.resepkita.ui.search.ShowDataActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.GridViewHolder> {

    private ArrayList<Category> list;

    public CategoryAdapter(ArrayList<Category> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_category, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.GridViewHolder holder, int position) {
        final Category category = list.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.categoryName)
        TextView title;
        @BindView(R.id.categoryThumb)
        ImageView img;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Category category) {
            title.setText(category.getName());
            Glide.with(itemView.getContext())
                    .load(category.getImg())
                    .into(img);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Category category = list.get(position);
            String c = category.getName();
            Intent intent = new Intent(itemView.getContext(), ShowDataActivity.class);
            intent.putExtra("category", c);
            itemView.getContext().startActivity(intent);
        }
    }
}
