package com.example.project.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class DiagnosticsAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    LayoutInflater inflater = null;

    public DiagnosticsAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView dateTimeName;
    }

    @Override
    public int getCount() {
        return data.length();
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
        JSONObject obj = data.optJSONObject(position);
        return null;
    }
}

