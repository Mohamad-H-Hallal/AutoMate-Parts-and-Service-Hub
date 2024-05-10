package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManagePartsActivity extends BaseActivity {
    private ImageButton backButton;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_manage_parts);
        backButton = findViewById(R.id.back_arrow2);
        fab = findViewById(R.id.fab);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {

            backButton.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {

            backButton.setImageResource(R.drawable.ic_back_ar);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagePartsActivity.this, AddPartsActivity.class));
            }
        });
    }
}