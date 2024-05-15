package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
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

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Controller.ScrapyardController;
import com.example.project.Controller.UserData;
import com.example.project.Model.ScrapyardModel;
import com.example.project.R;
import com.example.project.View.Activities.MapsLocationActivity;
import com.example.project.View.Activities.ProfileViewActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class ScrapYardAdapter extends BaseAdapter {

    List<ScrapyardModel> data;
    Context context;
    double latitude = 33;
    double longitude = 35;
    int phoneNumber = 70707070;
    private float currentRating = 3.5f;
    private AlertDialog ratingDialog;
    LayoutInflater inflater = null;

    public ScrapYardAdapter(Context context, List<ScrapyardModel> data) {
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
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
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
        ScrapyardModel obj = data.get(position);

        Glide.with(context).load(USER_IMAGES_DIR + obj.getIcon())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.profile_def_icon)
                .into(holder.scrapYardImageView);
        holder.txtScrapYardName.setText(obj.getName());
        holder.txtScrapYardSpecialization.setText(obj.getSpecialization());
        holder.rateScrapYardButton.setText(obj.getRating() +"/5");
        currentRating = obj.getRating();

        holder.locationScrapYardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapsLocationActivity.class);
                i.putExtra("latitude", obj.getLatitude());
                i.putExtra("longitude", obj.getLongitude());
                context.startActivity(i);
            }
        });
        holder.callScrapYardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + obj.getPhone());
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
                        ScrapyardController controller = new ScrapyardController();
                        controller.submitRating(context, currentRating, UserData.getId(), obj.getScrapyard_id(), new ScrapyardController.ScrapyardRateListener() {
                            @Override
                            public void onrateScrapyardDataReceived(Float rate) {
                                holder.rateScrapYardButton.setText(rate + "/5");
                                Toast.makeText(context, "Rating submitted: " + currentRating, Toast.LENGTH_SHORT).show();
                                dismissDialog();
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });

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
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileViewActivity.class);
                i.putExtra("id",obj.getScrapyard_id());
                i.putExtra("user_type",obj.getAccount_type());
                context.startActivity(i);
            }
        });
        return rowView;
    }

    private void dismissDialog() {
        if (ratingDialog != null && ratingDialog.isShowing()) {
            ratingDialog.dismiss();
        }
    }

}
