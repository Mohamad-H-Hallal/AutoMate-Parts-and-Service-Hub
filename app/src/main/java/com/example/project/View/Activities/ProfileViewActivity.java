package com.example.project.View.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.R;

public class ProfileViewActivity extends BaseActivity {
    RatingBar bar;
    ImageButton back;
    TextView location;
    AppCompatButton partsview;
    AppCompatButton rate;
    float currentRating;
    private AlertDialog ratingDialog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        bar = findViewById(R.id.v_rating_bar);
        bar.setRating((float)3.5);
        back = findViewById(R.id.back_arrow6);
        location = findViewById(R.id.v_locationtext);
        rate = findViewById(R.id.v_rating);
        partsview = findViewById(R.id.v_manageparts);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Intent i = new Intent(ProfileViewActivity.this, MapsLocationActivity.class);
                    i.putExtra("latitude", 33);
                    i.putExtra("longitude", 35);
                    startActivity(i);

                }

        });

        partsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileViewActivity.this,PartsCategoriesActivity.class);
                i.putExtra("viewer", 1);
                startActivity(i);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewActivity.this);
                View ratingDialogView = LayoutInflater.from(ProfileViewActivity.this).inflate(R.layout.rating_dialog, null);
                final RatingBar ratingBar = ratingDialogView.findViewById(R.id.rating_bar);
                final AppCompatButton submitButton = ratingDialogView.findViewById(R.id.submit_button);
                final AppCompatButton cancelButton = ratingDialogView.findViewById(R.id.cancel_button);
                ratingBar.setRating(currentRating);
                builder.setView(ratingDialogView);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentRating = ratingBar.getRating();
                        Toast.makeText(ProfileViewActivity.this, "Rating submitted: " + currentRating, Toast.LENGTH_SHORT).show();
                        if (ratingDialog != null && ratingDialog.isShowing()) {
                            ratingDialog.dismiss();
                        }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ratingDialog != null && ratingDialog.isShowing()) {
                            ratingDialog.dismiss();
                        }
                    }
                });
                ratingDialog = builder.create();
                ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingDialog.show();
            }
        });
    }



}