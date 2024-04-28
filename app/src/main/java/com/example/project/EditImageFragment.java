package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class EditImageFragment extends Fragment {

    private static final String ARG_IMAGE_RES = "image_res";

    private int imageRes;

    public static EditImageFragment newInstance(int imageRes) {
        EditImageFragment fragment = new EditImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES, imageRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageRes = getArguments().getInt(ARG_IMAGE_RES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_image_edit, container, false);
        ShapeableImageView imageView = rootView.findViewById(R.id.e_images_scroll);
        ShapeableImageView delete = rootView.findViewById(R.id.e_images_delete);
        Glide.with(getContext()).load(imageRes).into(imageView);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof EditPartActivity) {
                    ((EditPartActivity) getActivity()).deleteImage(imageRes);
                }
            }
        });
        return rootView;
    }
}

