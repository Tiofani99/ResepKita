package com.developer_ngapak.resepkita.ui.fragment.description;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer_ngapak.resepkita.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProsesFragment extends Fragment {


    public ProsesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proses, container, false);
    }

}
