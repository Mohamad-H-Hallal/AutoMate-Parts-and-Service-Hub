package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ViewDataActivity extends AppCompatActivity {

    ImageButton back;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        back = findViewById(R.id.back_arrow12);
        content = findViewById(R.id.dataContent);
        Intent i = getIntent();
        String con = i.getStringExtra("path");

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

        new DownloadTextTask().execute(IP + con);
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return downloadTextFile(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            content.setText(result);
        }

        private String downloadTextFile(String url) {
            String textContent = "";
            try {
                URL fileUrl = new URL(url);
                URLConnection connection = fileUrl.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                textContent = stringBuilder.toString();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return textContent;
        }
    }
}
