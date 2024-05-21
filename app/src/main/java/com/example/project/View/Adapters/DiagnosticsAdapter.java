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
import com.example.project.R;
import com.example.project.View.Activities.ViewDataActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DiagnosticsAdapter extends BaseAdapter {

    private ArrayList<File> files;
    Context context;
    LayoutInflater inflater = null;

    public DiagnosticsAdapter(Context context, ArrayList<File> files) {
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
        rowView = inflater.inflate(R.layout.row_diagnostics,null);
        holder.dateTimeName = rowView.findViewById(R.id.dateTimeName);
        File file = files.get(position);
        holder.dateTimeName.setText(file.getName());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String content = UnzipUtil.readFile(file);
                    Intent i = new Intent(context, ViewDataActivity.class);
                    i.putExtra("content",content);
                    context.startActivity(i);
                } catch (IOException e) {
                    Toast.makeText(context, "Failed to read file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return rowView;
    }
}

