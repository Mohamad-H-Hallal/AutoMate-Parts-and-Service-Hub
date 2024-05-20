package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.project.Controller.PartController;
import com.example.project.CustomMapView;
import com.example.project.Model.PartModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.R;
import com.example.project.View.Adapters.ImageEditAdapter;
import com.example.project.View.Adapters.ImageSliderAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class PartDetailsActivity extends BaseActivity {
    ViewPager horizontalScrollView;
    CustomMapView miniMapView;
    ImageButton back;
    ShapeableImageView location;
    TextView phone, name_part, pricetxt, scrapyardname, make_detailtxt, model_detailtxt, year_detailtxt, category_detailtxt, subcategory_detailtxt, condition_detailtxt, descriptiontxt;
    CheckBox negotiable_detail;
    CardView partDetailCardView;
    PartController part_controller;
    ArrayList<String> imageList;
    ImageSliderAdapter adapter;

    private double scrapyardLatitude;
    private double scrapyardLongitude;

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
        name_part = findViewById(R.id.name_part);
        pricetxt = findViewById(R.id.pricetxt);
        scrapyardname = findViewById(R.id.scrapyardname_detailtxt);
        make_detailtxt = findViewById(R.id.make_detailtxt);
        model_detailtxt = findViewById(R.id.model_detailtxt);
        year_detailtxt = findViewById(R.id.year_detailtxt);
        category_detailtxt = findViewById(R.id.category_detailtxt);
        subcategory_detailtxt = findViewById(R.id.subcategory_detailtxt);
        condition_detailtxt = findViewById(R.id.condition_detailtxt);
        descriptiontxt = findViewById(R.id.descriptiontxt);
        negotiable_detail = findViewById(R.id.negotiable_detail);
        partDetailCardView = findViewById(R.id.partDetailCardView);
        miniMapView = findViewById(R.id.miniMapView);
        part_controller = new PartController();

        Intent i = getIntent();
        String part_id = i.getStringExtra("part_id");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "en");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        part_controller.fetchPartDetails(this, Integer.parseInt(part_id), new PartController.PartDetailsFetchListener() {
            @Override
            public void onPartDetailsFetched(PartModel part, ScrapyardModel scrapyard) {
                // Set part details
                name_part.setText(part.getName());
                pricetxt.setText("USD " + part.getPrice());
                make_detailtxt.setText(part.getMake());
                model_detailtxt.setText(part.getModel());

                String[] cat = part.getCategory().split("-");
                String[] subcat = part.getSubcategory().split("-");
                String[] yea = part.getYear().split("-");
                String[] cond = part.getPart_condition().split("-");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PartDetailsActivity.this);
                String selectedLanguage = preferences.getString("selected_language", "en");
                if (selectedLanguage.equals("en")) {
                    year_detailtxt.setText(yea[0]);
                    category_detailtxt.setText(cat[0]);
                    subcategory_detailtxt.setText(subcat[0]);
                    condition_detailtxt.setText(cond[0]);
                } else if (selectedLanguage.equals("ar")) {
                    year_detailtxt.setText(yea[1]);
                    category_detailtxt.setText(cat[1]);
                    subcategory_detailtxt.setText(subcat[1]);
                    condition_detailtxt.setText(cond[1]);
                }

                descriptiontxt.setText(part.getDescription());
                negotiable_detail.setChecked(part.isNegotiable());
                // Set scrapyard details
                scrapyardname.setText(scrapyard.getName());
                phone.setText(scrapyard.getPhone());
                scrapyardLatitude = scrapyard.getLatitude();
                scrapyardLongitude = scrapyard.getLongitude();

                Log.d("test",scrapyardLatitude+" "+scrapyardLongitude);
                miniMapView.onCreate(savedInstanceState);
                miniMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng scrapyardLocation = new LatLng(scrapyardLatitude, scrapyardLongitude);
                        googleMap.addMarker(new MarkerOptions().position(scrapyardLocation).title(scrapyard.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scrapyardLocation, 12f));
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });

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

        imageList = new ArrayList<>();
        part_controller.getImagesPath(this, Integer.parseInt(part_id), new PartController.ImagePathListener() {
            @Override
            public void onImagePathReceived(ArrayList<String> imageUrls) {
                imageList = imageUrls;
                if (imageList.isEmpty()) {
                    partDetailCardView.setVisibility(View.VISIBLE);
                } else {
                    partDetailCardView.setVisibility(View.GONE);
                    adapter = new ImageSliderAdapter(PartDetailsActivity.this, imageList);
                    horizontalScrollView.setAdapter(adapter);
                }

            }
        });

        if (!imageList.isEmpty()) {
            partDetailCardView.setVisibility(View.GONE);
        }



        // Set up the map asynchronously



        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsLocationActivity.class);
                i.putExtra("latitude", scrapyardLatitude);
                i.putExtra("longitude", scrapyardLongitude);
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

        // Fetch part details

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
