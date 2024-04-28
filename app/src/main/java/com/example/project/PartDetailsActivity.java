package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class PartDetailsActivity extends BaseActivity {
    ViewPager horizontalScrollView;
    CustomMapView miniMapView;
    ImageButton back;
    ShapeableImageView location;
    TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_details);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        miniMapView = findViewById(R.id.miniMapView);
        back = findViewById(R.id.back_arrow4);
        location = findViewById(R.id.location_part_click);
        phone = findViewById(R.id.phone_detailtxt);

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


        List<String> imageList = new ArrayList<>();
        // Add your image resources to the list


        ImageSliderAdapter adapter = new ImageSliderAdapter(getSupportFragmentManager(), imageList);
        horizontalScrollView.setAdapter(adapter);


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
                LatLng location = new LatLng(33, 35);
                googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapsLocationActivity.class);
                i.putExtra("latitude",33);
                i.putExtra("longitude",35);
                startActivity(i);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = (String) phone.getText();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(i);
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