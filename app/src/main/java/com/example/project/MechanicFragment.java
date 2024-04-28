package com.example.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MechanicFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView mechanicsFilter;
    private TextView mechanicsFilterText;

    public MechanicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mechanic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.mechanicsSearch);
        mechanicsFilter = view.findViewById(R.id.mechanicsFilter);
        mechanicsFilterText = view.findViewById(R.id.mechanicsFilterText);
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

        mechanicsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mechanicsFilterText.getVisibility() == View.VISIBLE){
                    mechanicsFilterText.setVisibility(View.GONE);
                    mechanicsFilter.setBackground(null);
                }else {
                    mechanicsFilterText.setVisibility(View.VISIBLE);
                    mechanicsFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });
    }
}