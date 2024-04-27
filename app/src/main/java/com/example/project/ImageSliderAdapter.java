package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context mContext;
//    private List<String> mImageUrls;
private int[] mImages = {R.drawable.test, R.drawable.test, R.drawable.test};

//    public ImageSliderAdapter(Context context, List<String> imageUrls) {
    public ImageSliderAdapter(Context context) {
        mContext = context;
//        mImageUrls = imageUrls;
    }

    @Override
    public int getCount() {
//        return mImageUrls.size();
        return mImages.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_image_scroll, container, false);
        ShapeableImageView imageView = itemView.findViewById(R.id.images_scroll);
        imageView.setImageResource(R.drawable.test);
//        Glide.with(mContext).load(mImageUrls.get(position)).into(imageView);
        container.addView(itemView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ShapeableImageView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

