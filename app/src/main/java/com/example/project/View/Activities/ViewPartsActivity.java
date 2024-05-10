package com.example.project.View.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.R;

public class ViewPartsActivity extends AppCompatActivity {
    ImageButton backButton;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_view_parts);
        backButton = findViewById(R.id.back_arrow5);
        lv = findViewById(R.id.v_parts_list);

//        PartsAdapter adapter = new PartsAdapter(this, Parts.getParts());

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

    }
}