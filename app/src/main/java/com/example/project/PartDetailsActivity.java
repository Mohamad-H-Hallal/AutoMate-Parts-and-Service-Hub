package com.example.project;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PartDetailsActivity extends AppCompatActivity {
    HorizontalScrollView horizontalScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_part_details);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        LinearLayout imageContainer = findViewById(R.id.imageContainer);


//        List<String> imageUrls = fetchImageUrlsFromDatabase(); // Implement this method to fetch image URLs

//        int imageWidth = getResources().getDimensionPixelSize(R.dimen.image_width);
//        int imageHeight = getResources().getDimensionPixelSize(R.dimen.image_height);
//
//        for (String imageUrl : imageUrls) {
//            ShapeableImageView imageView = new ShapeableImageView(this);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            Glide.with(this).load(imageUrl).into(imageView); // Use Glide or any other image loading library to load images from URL
//
//            CardView cardView = new CardView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(8, 8, 8, 8); // Set margins as needed
//            cardView.setLayoutParams(layoutParams);
//            cardView.setRadius(getResources().getDimension(R.dimen.card_corner_radius));
//            cardView.setElevation(getResources().getDimension(R.dimen.card_elevation));
//            // Add ImageView to CardView
//            cardView.addView(imageView);
//
//            // Add CardView to the LinearLayout container
//            imageContainer.addView(cardView);
//        }

    }
}