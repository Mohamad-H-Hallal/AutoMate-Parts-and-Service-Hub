package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PartDetailsActivity extends BaseActivity {
    HorizontalScrollView horizontalScrollView;
    CustomMapView miniMapView;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_details);

        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        LinearLayout imageContainer = findViewById(R.id.imageContainer);
        miniMapView = findViewById(R.id.miniMapView);
        back = findViewById(R.id.back_arrow4);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        miniMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        miniMapView.setScrollable(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        miniMapView.setScrollable(true);
                        break;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


        miniMapView = findViewById(R.id.miniMapView);

// Set up the map asynchronously
        miniMapView.onCreate(savedInstanceState);
        miniMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Customize the map as needed
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33, 35), 12));
            }
        });
    }

    // Remember to manage the lifecycle of the MapView
    @Override
    protected void onResume() {
        super.onResume();
        miniMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        miniMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        miniMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        miniMapView.onLowMemory();
    }


}