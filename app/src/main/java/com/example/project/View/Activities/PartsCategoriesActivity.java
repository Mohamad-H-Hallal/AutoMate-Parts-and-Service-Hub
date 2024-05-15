package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.example.project.View.Adapters.PartsCategoriesAdapter;

import java.util.HashMap;
import java.util.Map;

public class PartsCategoriesActivity extends BaseActivity {
    private ExpandableListView parts_list;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_parts_categories);

        back = findViewById(R.id.back_arrow1);
        parts_list = findViewById(R.id.categories_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        String[] parentList = getResources().getStringArray(R.array.categories);
        String[] subcategories = getResources().getStringArray(R.array.subcategories);

        Map<String, String[]> childMap =  new HashMap<>();
        for (int i = 0; i < parentList.length; i++) {
            childMap.put(parentList[i], subcategories[i].split(";"));
        }

        Intent i = getIntent();
        int v = i.getIntExtra("viewer", 0);

        PartsCategoriesAdapter adapter = new PartsCategoriesAdapter(this,parentList, childMap,v);
        parts_list.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }



}