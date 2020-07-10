package com.developer_ngapak.resepkita.ui.about;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.detail.DetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder> {

    private ArrayList<Developer> list = new ArrayList<>();

    public DeveloperAdapter() {
    }


    public void setData(ArrayList<Developer> developers) {
        list.clear();
        list.addAll(developers);
        notifyDataSetChanged();
        Log.d("Coba","Ukuran"+list.size());
    }


    @NonNull
    @Override
    public DeveloperAdapter.DeveloperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_developer, parent, false);
        return new DeveloperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeveloperAdapter.DeveloperViewHolder holder, int position) {
        final Developer developer = list.get(position);
        holder.bind(developer);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DeveloperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.civ_image_photo_developer)
        ImageView civPhoto;
//        @BindView(R.id.tv_name_developer)
//        TextView tvNameDeveloper;
//        @BindView(R.id.tv_nim_developer)
//        TextView tvNimDeveloper;


        public DeveloperViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Developer developer) {
            Log.d("Coba","Nama"+developer.getName());
//            tvNameDeveloper.setText(developer.getName());
//            tvNimDeveloper.setText(developer.getQuotes());
            Glide.with(itemView)
                    .load(developer.getPhoto())
                    .into(civPhoto);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Developer developer = list.get(position);
            Intent intent = new Intent(itemView.getContext(), DeveloperDetail.class);
            intent.putExtra(DeveloperDetail.EXTRA_DETAIL_DEVELOPER, developer);
            itemView.getContext().startActivity(intent);
        }
    }
}
