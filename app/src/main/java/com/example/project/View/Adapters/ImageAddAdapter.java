package com.example.project.View.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.View.Activities.AddPartsActivity;
import com.example.project.View.Activities.LoginActivity;
import com.example.project.View.Activities.SettingActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ImageAddAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> images;// Replace with your image data source (e.g., URLs or file paths)
    private OnImageRemoveListener mListener;
    private AlertDialog Dialog;


    public ImageAddAdapter(Context context, ArrayList<String> images, OnImageRemoveListener listener) {
        this.context = context;
        this.images = images;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_image_edit, container, false);
        ShapeableImageView imageView = view.findViewById(R.id.e_images_scroll);
        Glide.with(context).load(images.get(position)).into(imageView); // Using Glide for image loading

        ShapeableImageView deleteButton = view.findViewById(R.id.e_images_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View DialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                final AppCompatButton yesButton = DialogView.findViewById(R.id.del_yes_button);
                final AppCompatButton noButton = DialogView.findViewById(R.id.del_no_button);
                builder.setView(DialogView);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onImageRemoved(position);
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog = builder.create();
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Dialog.show();
            }
        });

        container.addView(view);
        return view;
    }


    public interface OnImageRemoveListener {
        void onImageRemoved(int position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}



