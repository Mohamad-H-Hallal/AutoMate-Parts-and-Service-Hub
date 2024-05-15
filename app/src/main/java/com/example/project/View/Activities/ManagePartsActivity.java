package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Controller.PartController;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Adapters.ManagePartsAdapter;
import com.example.project.View.Adapters.PartsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManagePartsActivity extends BaseActivity {

    private ImageButton backButton;
    private FloatingActionButton fab;
    private ListView parts_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_manage_parts);
        backButton = findViewById(R.id.back_arrow2);
        fab = findViewById(R.id.fab);
        parts_list = findViewById(R.id.parts_list);

        Intent intent = getIntent();
        String subcategory = intent.getStringExtra("subcategory");

        PartController partCont = new PartController();
        partCont.manageParts(this, subcategory, new PartController.PartManageListener() {
            @Override
            public void onPartsManage(List<PartModel> parts, ArrayList<String> imagePaths) {
                ManagePartsAdapter adapter = new ManagePartsAdapter(ManagePartsActivity.this, parts, imagePaths);
                parts_list.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagePartsActivity.this, AddPartsActivity.class));
            }
        });
    }
}
