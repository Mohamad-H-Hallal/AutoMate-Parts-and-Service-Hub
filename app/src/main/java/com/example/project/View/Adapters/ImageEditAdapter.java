package com.example.project.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ImageEditAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imagesfromuser;// Replace with your image data source (e.g., URLs or file paths)
    private ArrayList<String> imagesfromdb;
    private OnImageRemoveListener mListener;


    public ImageEditAdapter(Context context, ArrayList<String> imagesfromdb, ArrayList<String> imagesfromuser,OnImageRemoveListener listener) {
        this.context = context;
        this.imagesfromuser = imagesfromuser;
        this.mListener = listener;
        this.imagesfromdb = imagesfromdb;
    }

    @Override
    public int getCount() {
        return imagesfromuser.size()+imagesfromdb.size();
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

        String imagePath;
        if (position < imagesfromdb.size()) {
            if(!imagesfromdb.isEmpty()){
            imagePath = imagesfromdb.get(position);
            Glide.with(context).load(Parts_IMAGES_DIR + imagePath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.test)
                    .into(imageView);}
        } else {

            int adjustedPosition = position - imagesfromdb.size();
            imagePath = imagesfromuser.get(adjustedPosition);
            Glide.with(context).load(imagePath).into(imageView);

        }

        ShapeableImageView deleteButton = view.findViewById(R.id.e_images_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Delete image");
                                                builder.setMessage("Are you sure?");
                                                builder.setIcon(R.drawable.delete_icon);
                                                builder.setPositiveButton("Yes", (dialog, which) -> {
                                                    mListener.onImageRemoved(position);
                                                });
                                                builder.setNegativeButton("No", null);
                                                builder.show();

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



