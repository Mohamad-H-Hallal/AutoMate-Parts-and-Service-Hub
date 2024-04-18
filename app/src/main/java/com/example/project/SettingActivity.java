package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class SettingActivity extends BaseActivity {
    private ImageButton  back;
    private AppCompatButton save;
    private Spinner lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        back = findViewById(R.id.back_arrow3);
        save = findViewById(R.id.changel);
        lang = findViewById(R.id.lang);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String language = lang.getSelectedItem().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                if (language.equals("English")||language.equals("الإنجليزية")) {
                    editor.putString("selected_language", "en");

                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    SettingActivity.this.getResources().updateConfiguration(config, SettingActivity.this.getResources().getDisplayMetrics());


                } else if (language.equals("Arabic")||language.equals("العربية")) {
                    editor.putString("selected_language", "ar");

                    Locale locale = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    SettingActivity.this.getResources().updateConfiguration(config, SettingActivity.this.getResources().getDisplayMetrics());
                }
                editor.apply();
                restartApp();
            }


        });

    }

    private void restartApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }




}