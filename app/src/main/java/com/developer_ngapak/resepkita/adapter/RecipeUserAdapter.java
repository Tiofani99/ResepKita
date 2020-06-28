package com.developer_ngapak.resepkita.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.entity.Food;
import com.developer_ngapak.resepkita.ui.add_recipe.AddRecipeActivity;
import com.developer_ngapak.resepkita.ui.detail.DetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class RecipeUserAdapter extends RecyclerView.Adapter<RecipeUserAdapter.ViewHolder> {

    private ArrayList<Food> listFood;
    private Context mContext;
    DatabaseReference databaseReference;

    public RecipeUserAdapter(Context mContext, ArrayList<Food> listFood) {
        this.mContext = mContext;
        this.listFood = listFood;
    }

    @NonNull
    @Override
    public RecipeUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_user, parent, false);
        return new RecipeUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeUserAdapter.ViewHolder holder, int position) {
        final Food food = listFood.get(position);
        holder.bind(food);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_RECIPE, food);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView imgPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            itemView.setOnLongClickListener(this);
        }

        void bind(Food food) {
            Glide.with(itemView.getContext())
                    .load(food.getImg())
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            Food food = listFood.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            String update = mContext.getResources().getString(R.string.update);
            String delete = mContext.getResources().getString(R.string.delete);
            String[] options = {update, delete};

            builder.setItems(options, (dialogInterface, i) -> {
                if (i == 0) {
                    Intent intent = new Intent(itemView.getContext(), AddRecipeActivity.class);
                    intent.putExtra(AddRecipeActivity.EXTRA_RECIPE, food);
                    itemView.getContext().startActivity(intent);

                } else if (i == 1) {
                    showDeleteDialog(food.getName(), food.getImg());
                }
            });

            builder.create().show();

            return false;

        }


        private void showDeleteDialog(final String currentTitle, final String currentImage) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Data_by_User");
            databaseReference.keepSynced(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String delete = mContext.getResources().getString(R.string.delete);
            builder.setTitle(delete);
            String msg = mContext.getResources().getString(R.string.delete_message);
            builder.setMessage(msg);
            String yes = mContext.getResources().getString(R.string.yes);
            String no = mContext.getResources().getString(R.string.no);
            builder.setPositiveButton(yes, (dialogInterface, i) -> {
                Query mQuery = databaseReference.orderByChild("name").equalTo(currentTitle);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }

                        String deleteSuccess = mContext.getResources().getString(R.string.delete_success);
                        Toast.makeText(mContext, deleteSuccess, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                StorageReference mImage = getInstance().getReferenceFromUrl(currentImage);
                mImage.delete().addOnSuccessListener(aVoid -> {
                    //Toast.makeText(MainActivity.this,"Gambar berhasil dihapus",Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            builder.setNegativeButton(no, (dialogInterface, i) -> dialogInterface.dismiss());

            builder.create().show();

        }

    }
}