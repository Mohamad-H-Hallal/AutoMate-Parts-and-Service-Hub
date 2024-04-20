package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartsCategoriesActivity extends BaseActivity {
    private ExpandableListView parts_list;
    private ExpandableListAdapter PartsCategoriesAdapter;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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

        List<String> parentList = new ArrayList<>();
        parentList.add("Parent 1");
        parentList.add("Parent 2");

        Map<String, List<String>> childMap = new HashMap<>();
        childMap.put("Parent 1", Arrays.asList("Child 1", "Child 2"));
        childMap.put("Parent 2", Arrays.asList("Child 3", "Child 4"));

        PartsCategoriesAdapter adapter = new PartsCategoriesAdapter(this,parentList, childMap);
        parts_list.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}