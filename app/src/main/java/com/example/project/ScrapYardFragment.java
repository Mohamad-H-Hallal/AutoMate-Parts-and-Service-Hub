package com.example.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScrapYardFragment extends BaseFragment {

    private SearchView searchView;
    private ImageView scrapYardFilter;
    private TextView scrapYardFilterText;

    public ScrapYardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scrap_yard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.scrapYardSearch);
        scrapYardFilter = view.findViewById(R.id.scrapYardFilter);
        scrapYardFilterText = view.findViewById(R.id.scrapYardFilterText);
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

        scrapYardFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrapYardFilterText.getVisibility() == View.VISIBLE){
                    scrapYardFilterText.setVisibility(View.GONE);
                    scrapYardFilter.setBackground(null);
                }else {
                    scrapYardFilterText.setVisibility(View.VISIBLE);
                    scrapYardFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });
    }
}