package com.example.project.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.View.Activities.ManagePartsActivity;
import com.example.project.View.Activities.ViewPartsActivity;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PartsCategoriesAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[] parentList;
    private Map<String, String[]> childMap;
    private int viewer;

    public PartsCategoriesAdapter(Context context, String[] parentList, Map<String, String[]> childMap, int viewer) {
        this.context = context;
        this.parentList = parentList;
        this.childMap = childMap;
        this.viewer = viewer;
        updateData();
    }

    public void updateData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString("selected_language", "");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(childMap.get(parentList[groupPosition]))[childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_subcategories, null);
        }
        TextView txtListChild = convertView.findViewById(R.id.subcategories_name);
        txtListChild.setText(childText);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewer == 0) {
                    Intent i = new Intent(context, ManagePartsActivity.class);
                    i.putExtra("categories", childText);
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, ViewPartsActivity.class);
                    i.putExtra("categories", childText);
                    context.startActivity(i);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(childMap.get(parentList[groupPosition])).length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return parentList.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String parentText = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_parts_categories, null);
        }
        TextView lblListHeader = convertView.findViewById(R.id.categories_name);
        lblListHeader.setText(parentText);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}