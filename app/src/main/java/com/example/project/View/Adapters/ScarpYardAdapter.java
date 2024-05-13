package com.example.project.View.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.project.R;
import com.example.project.View.Activities.MapsLocationActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class ScarpYardAdapter extends BaseAdapter {

    JSONArray data;
    Context context;
    double latitude = 33;
    double longitude = 35;
    int phoneNumber = 70707070;
    private float currentRating = 3.5f;
    private AlertDialog ratingDialog;
    LayoutInflater inflater = null;

    public ScarpYardAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        updateData();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class Holder {
        ShapeableImageView scrapYardImageView;
        TextView txtScrapYardName, txtScrapYardSpecialization;
        AppCompatButton callScrapYardButton, locationScrapYardButton, rateScrapYardButton;
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
        rowView = inflater.inflate(R.layout.row_scrapyard, null);
        holder.scrapYardImageView = rowView.findViewById(R.id.scrapYardImageView);
        holder.txtScrapYardName = rowView.findViewById(R.id.txtScrapYardName);
        holder.txtScrapYardSpecialization = rowView.findViewById(R.id.txtScrapYardSpecialization);
        holder.callScrapYardButton = rowView.findViewById(R.id.callScrapYardButton);
        holder.locationScrapYardButton = rowView.findViewById(R.id.locationScrapYardButton);
        holder.rateScrapYardButton = rowView.findViewById(R.id.rateScrapYardButton);
        JSONObject obj = data.optJSONObject(position);
        holder.locationScrapYardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapsLocationActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                context.startActivity(i);
            }
        });
        holder.callScrapYardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + phoneNumber);
                Intent i = new Intent(Intent.ACTION_DIAL, number);
                context.startActivity(i);
            }
        });
        holder.rateScrapYardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View ratingDialogView = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null);
                final RatingBar ratingBar = ratingDialogView.findViewById(R.id.rating_bar);
                final AppCompatButton submitButton = ratingDialogView.findViewById(R.id.submit_button);
                final AppCompatButton cancelButton = ratingDialogView.findViewById(R.id.cancel_button);
                ratingBar.setRating(currentRating);
                builder.setView(ratingDialogView);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentRating = ratingBar.getRating();
                        holder.rateScrapYardButton.setText(currentRating + "");
                        Toast.makeText(context, "Rating submitted: " + currentRating, Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                    }
                });
                ratingDialog = builder.create();
                ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingDialog.show();
            }
        });
        return null;
    }

    private void dismissDialog() {
        if (ratingDialog != null && ratingDialog.isShowing()) {
            ratingDialog.dismiss();
        }
    }

}