package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.project.Controller.PartController;
import com.example.project.CustomMapView;
import com.example.project.R;
import com.example.project.View.Adapters.ImageSliderAdapter;
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
    CardView partDetailCardView;
    PartController part_controller;

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
        partDetailCardView = findViewById(R.id.partDetailCardView);
        part_controller = new PartController();

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

        ArrayList<String> imageList = new ArrayList<>();
//        imageList = part_controller.imagespath(this,lid lal part);



        if(!imageList.isEmpty()){
            partDetailCardView.setVisibility(View.GONE);
        }
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageList);
        horizontalScrollView.setAdapter(adapter);

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