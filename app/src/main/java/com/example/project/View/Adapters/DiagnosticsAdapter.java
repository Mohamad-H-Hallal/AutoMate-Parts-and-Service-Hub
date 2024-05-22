package com.example.project.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Controller.UnzipUtil;
import com.example.project.Model.DiagnosticDataModel;
import com.example.project.R;
import com.example.project.View.Activities.ViewDataActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticsAdapter extends BaseAdapter {

    private List<DiagnosticDataModel> files;
    Context context;
    LayoutInflater inflater = null;

    public DiagnosticsAdapter(Context context, List<DiagnosticDataModel> files) {
        this.context = context;
        this.files = files;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView dateTimeName;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row_diagnostics, null);
        holder.dateTimeName = rowView.findViewById(R.id.dateTimeName);
        holder.dateTimeName.setText(files.get(position).getDate_time());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewDataActivity.class);
                i.putExtra("path", files.get(position).getFile());
                context.startActivity(i);
            }
        });
        return rowView;
    }
}

