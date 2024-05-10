package com.example.project.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.project.R;

public class PartsFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView partsFilter;
    private CardView partsCardFilter;

    public PartsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.partsSearch);
        partsFilter = view.findViewById(R.id.partsFilter);
        partsCardFilter = view.findViewById(R.id.partsCardFilter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return false;
            }
        });

        partsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partsCardFilter.getVisibility() == View.VISIBLE){
                    partsCardFilter.setVisibility(View.GONE);
                    partsFilter.setBackground(null);
                }else {
                    partsCardFilter.setVisibility(View.VISIBLE);
                    partsFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });
    }

}