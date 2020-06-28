package com.developer_ngapak.resepkita.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer_ngapak.resepkita.MainActivity;
import com.developer_ngapak.resepkita.R;
import com.developer_ngapak.resepkita.adapter.RecipeUserAdapter;
import com.developer_ngapak.resepkita.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFragment extends Fragment {


    private FirebaseAuth mAuth;

    @BindView(R.id.tv_logout)
    TextView btnLogout;
    @BindView(R.id.rv_food)
    RecyclerView rvFood;
    @BindView(R.id.tv_name_user)
    TextView tvNameUser;
    @BindView(R.id.image_profile)
    ImageView imgUser;
    @BindView(R.id.tv_no_post)
    TextView tvNoData;
    private String username;
    private String image;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,root);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        String id = user.getUid();
        showData(id);
        ShowDataUser(id);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        mDatabase.keepSynced(true);


        btnLogout = root.findViewById(R.id.tv_logout);
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        return root;
    }

    private void ShowDataUser(String str) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.orderByChild("id")
                .startAt(str)
                .endAt(str + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                username = Objects.requireNonNull(ds.child("fullname").getValue()).toString();
                                image = Objects.requireNonNull(ds.child("imageurl").getValue()).toString();
                            }

                            tvNameUser.setText(username);
                            Glide.with(requireActivity())
                                    .load(image)
                                    .into(imgUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showData(String str) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Data_by_User");
        mRef.orderByChild("id")
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
                        } else {
                            tvNoData.setVisibility(getView().VISIBLE);
                        }

                        tvNoData.setVisibility(getView().GONE);
                        Collections.reverse(myList);
                        RecipeUserAdapter cardViewAdapter = new RecipeUserAdapter(getActivity(), myList);
                        rvFood.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, true));
                        rvFood.setAdapter(cardViewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


}