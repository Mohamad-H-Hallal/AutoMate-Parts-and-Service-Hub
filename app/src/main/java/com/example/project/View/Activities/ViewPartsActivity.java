package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.Controller.PartController;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Adapters.ManagePartsAdapter;
import com.example.project.View.Adapters.PartsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPartsActivity extends BaseActivity {
    ImageButton backButton;
    ListView lv;
    String subcategory,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_view_parts);
        backButton = findViewById(R.id.back_arrow5);
        lv = findViewById(R.id.v_parts_list);

        Intent intent = getIntent();
        subcategory = intent.getStringExtra("subcategory");
        category = intent.getStringExtra("category");

        PartController partCont = new PartController();
        partCont.manageParts(this, subcategory, new PartController.PartManageListener() {
            @Override
            public void onPartsManage(List<PartModel> parts, ArrayList<String> imagePaths) {
                PartsAdapter adapter = new PartsAdapter(ViewPartsActivity.this, parts, imagePaths);
                lv.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ViewPartsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

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