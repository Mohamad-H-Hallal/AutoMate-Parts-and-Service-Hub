package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import static com.example.project.Configuration.Parts_IMAGES_DIR;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageEditAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> images;// Replace with your image data source (e.g., URLs or file paths)
    private OnImageRemoveListener mListener;


    public ImageEditAdapter(Context context, ArrayList<String> images,OnImageRemoveListener listener) {
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
        Glide.with(context).load(Parts_IMAGES_DIR + images.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.test)
                .into(imageView);

        ShapeableImageView deleteButton = view.findViewById(R.id.e_images_delete);
        deleteButton.setOnClickListener(v -> mListener.onImageRemoved(position));

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



