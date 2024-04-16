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
        ShapeableImageView scrapYardImageView;
        TextView txtScrapYardName, txtScrapYardSpecialization;
        AppCompatButton callScrapYardButton, locationMechanicButton, rateScrapYardButton;
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
        holder.scrapYardImageView = rowView.findViewById(R.id.scrapYardImageView);
        holder.txtScrapYardName = rowView.findViewById(R.id.txtScrapYardName);
        holder.txtScrapYardSpecialization = rowView.findViewById(R.id.txtScrapYardSpecialization);
        holder.callScrapYardButton = rowView.findViewById(R.id.callScrapYardButton);
        holder.locationMechanicButton = rowView.findViewById(R.id.locationMechanicButton);
        holder.rateScrapYardButton = rowView.findViewById(R.id.rateScrapYardButton);
        JSONObject obj = data.optJSONObject(position);
        return null;
    }
}
