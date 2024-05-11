package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imagesfromdb;


    public ImageSliderAdapter(Context context, ArrayList<String> imagesfromdb) {
        this.context = context;
        this.imagesfromdb = imagesfromdb;
    }

    @Override
    public int getCount() {
        return imagesfromdb.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_image_scroll, container, false);
        ShapeableImageView imageView = view.findViewById(R.id.images_scroll);

            if(!imagesfromdb.isEmpty()){
            Glide.with(context).load(Parts_IMAGES_DIR + imagesfromdb.get(position))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.test)
                    .into(imageView);}
            container.addView(view);
        return view;
    }

}

