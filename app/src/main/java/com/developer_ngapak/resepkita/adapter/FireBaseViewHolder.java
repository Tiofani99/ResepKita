package com.developer_ngapak.resepkita.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer_ngapak.resepkita.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FireBaseViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public ImageView img;

    public FireBaseViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tv_item_name);
        img = itemView.findViewById(R.id.img_item_photo);
    }
}
