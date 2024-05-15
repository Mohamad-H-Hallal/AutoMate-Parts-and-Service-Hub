package com.example.project.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.project.Controller.ScrapyardController;
import com.example.project.Controller.UserData;
import com.example.project.Model.ScrapyardModel;
import com.example.project.R;
import com.example.project.View.Adapters.ScrapYardAdapter;

import java.util.List;

public class ScrapYardFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView scrapYardFilter;
    private TextView scrapYardFilterText;
    ScrapyardController controller;
    boolean isfiltered = false;
    ScrapYardAdapter adapter;
    private ListView scrapYardListView;
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
        scrapYardListView = view.findViewById(R.id.scrapYardListView);
        controller = new ScrapyardController();

        controller.getAllScrapyardsData(requireContext(), String.valueOf(UserData.getId()), "false", new ScrapyardController.AllScrapyardDataListener() {
            @Override
            public void onAllScrapyardDataReceived(List<ScrapyardModel> all_scrapyard) {
                adapter = new ScrapYardAdapter(requireContext(),all_scrapyard);
                scrapYardListView.setAdapter(adapter);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

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
                isfiltered = !isfiltered;
                if(isfiltered){
                    scrapYardFilterText.setVisibility(View.VISIBLE);
                    scrapYardFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }else {
                    scrapYardFilterText.setVisibility(View.GONE);
                    scrapYardFilter.setBackground(null);
                }
                controller.getAllScrapyardsData(requireContext(), String.valueOf(UserData.getId()), String.valueOf(isfiltered), new ScrapyardController.AllScrapyardDataListener() {
                    @Override
                    public void onAllScrapyardDataReceived(List<ScrapyardModel> all_scrapyard) {
                        adapter = new ScrapYardAdapter(requireContext(), all_scrapyard);
                        scrapYardListView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            }
        });
    }
}