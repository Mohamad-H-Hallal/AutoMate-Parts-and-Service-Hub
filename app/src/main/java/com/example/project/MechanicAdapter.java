package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class MechanicAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    LayoutInflater inflater = null;

    public MechanicAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        updateData();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView mechanicImageView;
        TextView txtMechanicName, txtMechanicSpecialization;
        AppCompatButton callMechanicButton, locationMechanicButton, rateMechanicButton;
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
