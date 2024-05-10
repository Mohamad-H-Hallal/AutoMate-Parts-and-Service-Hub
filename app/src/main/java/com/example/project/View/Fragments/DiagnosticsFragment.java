package com.example.project.View.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.project.R;

public class DiagnosticsFragment extends BaseFragment {

    private ImageView carDataFilter;
    private CardView carDataCardFilter;
    private AppCompatButton importData;

    public DiagnosticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnostics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carDataFilter = view.findViewById(R.id.carDataFilter);
        carDataCardFilter = view.findViewById(R.id.carDataCardFilter);
        carDataFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carDataCardFilter.getVisibility() == View.VISIBLE){
                    carDataCardFilter.setVisibility(View.GONE);
                    carDataFilter.setBackground(null);
                }else {
                    carDataCardFilter.setVisibility(View.VISIBLE);
                    carDataFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });
    }
}