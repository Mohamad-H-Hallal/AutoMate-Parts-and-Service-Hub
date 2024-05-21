package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class ViewDataActivity extends AppCompatActivity {

    ImageButton back;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        back = findViewById(R.id.back_arrow12);
        content = findViewById(R.id.dataContent);
        Intent i = getIntent();
        String con = i.getStringExtra("content");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "en");
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

        content.setText(con);

    }
}