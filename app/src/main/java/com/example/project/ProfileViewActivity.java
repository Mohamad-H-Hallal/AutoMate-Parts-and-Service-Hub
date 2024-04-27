package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileViewActivity extends AppCompatActivity {
    RatingBar bar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        bar = findViewById(R.id.v_rating_bar);
        bar.setRating((float)3.5);
    }
}