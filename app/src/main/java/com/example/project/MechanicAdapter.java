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

public class MechanicAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    LayoutInflater inflater = null;

    public MechanicAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView mechanicImageView;
        TextView txtMechanicName, txtMechanicSpecialization;
        AppCompatButton callMechanicButton, locationMechanicButton, rateMechanicButton;
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
        rowView = inflater.inflate(R.layout.row_mechanic,null);
        holder.mechanicImageView = rowView.findViewById(R.id.mechanicImageView);
        holder.txtMechanicName = rowView.findViewById(R.id.txtMechanicName);
        holder.txtMechanicSpecialization = rowView.findViewById(R.id.txtMechanicSpecialization);
        holder.callMechanicButton = rowView.findViewById(R.id.callMechanicButton);
        holder.locationMechanicButton = rowView.findViewById(R.id.locationMechanicButton);
        holder.rateMechanicButton = rowView.findViewById(R.id.rateMechanicButton);
        JSONObject obj = data.optJSONObject(position);
        return null;
    }
}
