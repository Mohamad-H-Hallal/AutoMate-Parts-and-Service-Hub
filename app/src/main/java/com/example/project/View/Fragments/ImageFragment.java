package com.example.project.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project.R;

public class ImageFragment extends Fragment {

    private static final String ARG_IMAGE_RES = "image_url";

    private String imageRes;

    public static ImageFragment newInstance(String imageRes) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageRes = getArguments().getString(ARG_IMAGE_RES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_image_scroll, container, false);
        ImageView imageView = rootView.findViewById(R.id.images_scroll);
        Glide.with(getContext()).load(imageRes).into(imageView);
        return rootView;
    }
}
