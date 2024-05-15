package com.example.project.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.project.Controller.MechanicController;
import com.example.project.Controller.UserData;
import com.example.project.Model.MechanicModel;
import com.example.project.R;
import com.example.project.View.Adapters.MechanicAdapter;

import java.util.ArrayList;
import java.util.List;

public class MechanicFragment extends BaseFragment {

    private androidx.appcompat.widget.SearchView searchView;
    private ImageView mechanicsFilter;
    private TextView mechanicsFilterText;
    public String filtered="false";
    MechanicController controller;
    private ListView mechanicsListView;
    MechanicAdapter adapter;

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
        mechanicsListView = view.findViewById(R.id.mechanicsListView);
        controller=new MechanicController();

        controller.getAllMechanicsData(requireContext(),String.valueOf(UserData.getId()), filtered ,new MechanicController.AllMechanicDataListener() {
            @Override
            public void onAllMechanicDataReceived(List<MechanicModel> all_mechanics) {
                adapter = new MechanicAdapter(requireContext(),all_mechanics);
                mechanicsListView.setAdapter(adapter);
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("error",error.getMessage());
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

        mechanicsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mechanicsFilterText.getVisibility() == View.VISIBLE){
                    mechanicsFilterText.setVisibility(View.GONE);
                    mechanicsFilter.setBackground(null);
                    filtered="false";
                    controller.getAllMechanicsData(requireContext(),String.valueOf(UserData.getId()), filtered, new MechanicController.AllMechanicDataListener() {
                        @Override
                        public void onAllMechanicDataReceived(List<MechanicModel> all_mechanics) {
                            adapter = new MechanicAdapter(requireContext(),all_mechanics);
                            mechanicsListView.setAdapter(adapter);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("error",error.toString());
                        }
                    });

                }else {
                    mechanicsFilterText.setVisibility(View.VISIBLE);
                    mechanicsFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                    filtered="true";
                    controller.getAllMechanicsData(requireContext(),  String.valueOf(UserData.getId()),filtered, new MechanicController.AllMechanicDataListener() {
                        @Override
                        public void onAllMechanicDataReceived(List<MechanicModel> all_mechanics) {
                            adapter = new MechanicAdapter(requireContext(),all_mechanics);
                            mechanicsListView.setAdapter(adapter);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.e("error",error.toString());
                        }
                    });

                }
            }
        });
    }
}