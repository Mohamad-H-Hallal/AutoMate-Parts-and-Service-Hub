package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScarpYardAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    LayoutInflater inflater = null;

    public ScarpYardAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView imageView;
        TextView txt1, txt2;
        AppCompatButton btnCall, btnLocation, btnRate;
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
        rowView = inflater.inflate(R.layout.row_scrapyard,null);
        holder.imageView = rowView.findViewById(R.id.partImageView);
        holder.txt1 = rowView.findViewById(R.id.txtName);
        holder.txt2 = rowView.findViewById(R.id.txtSpecialization);
        holder.btnCall = rowView.findViewById(R.id.callButton);
        holder.btnLocation = rowView.findViewById(R.id.locationButton);
        holder.btnRate = rowView.findViewById(R.id.rateButton);
        JSONObject obj = data.optJSONObject(position);
        return null;
    }
}
