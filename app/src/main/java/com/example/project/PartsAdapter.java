package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class PartsAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    LayoutInflater inflater = null;

    public PartsAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView partsImageView;
        TextView txtPartName, txtMake, txtYear, txtModel, txtCategory, txtPrice, txtNegotiable, txtCondition;
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
        rowView = inflater.inflate(R.layout.row_parts,null);
        holder.partsImageView = rowView.findViewById(R.id.partImageView);
        holder.txtPartName = rowView.findViewById(R.id.txtPartName);
        holder.txtMake = rowView.findViewById(R.id.txtMake);
        holder.txtYear = rowView.findViewById(R.id.txtYear);
        holder.txtModel = rowView.findViewById(R.id.txtModel);
        holder.txtCategory = rowView.findViewById(R.id.txtCategory);
        holder.txtPrice = rowView.findViewById(R.id.txtPrice);
        holder.txtNegotiable = rowView.findViewById(R.id.txtNegotiable);
        holder.txtCondition = rowView.findViewById(R.id.txtCondition);
        JSONObject obj = data.optJSONObject(position);
        return null;
    }
}
