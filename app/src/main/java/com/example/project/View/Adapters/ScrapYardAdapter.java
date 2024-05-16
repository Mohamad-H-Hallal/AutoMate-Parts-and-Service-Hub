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
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScrapYardAdapter extends BaseAdapter implements Filterable {

    private List<ScrapyardModel> data;
    private List<ScrapyardModel> filteredData;
    private Context context;
    private LayoutInflater inflater;
    private float currentRating = 3.5f;
    private AlertDialog ratingDialog;
    private ItemFilter mFilter = new ItemFilter();

    public ScrapYardAdapter(Context context, List<ScrapyardModel> data) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
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
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;
        if (rowView == null) {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.row_scrapyard, parent, false);
            holder.scrapYardImageView = rowView.findViewById(R.id.scrapYardImageView);
            holder.txtScrapYardName = rowView.findViewById(R.id.txtScrapYardName);
            holder.txtScrapYardSpecialization = rowView.findViewById(R.id.txtScrapYardSpecialization);
            holder.callScrapYardButton = rowView.findViewById(R.id.callScrapYardButton);
            holder.locationScrapYardButton = rowView.findViewById(R.id.locationScrapYardButton);
            holder.rateScrapYardButton = rowView.findViewById(R.id.rateScrapYardButton);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        ScrapyardModel obj = filteredData.get(position);

        Glide.with(context).load(USER_IMAGES_DIR + obj.getIcon())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.profile_def_icon)
                .into(holder.scrapYardImageView);
        holder.txtScrapYardName.setText(obj.getName());
        holder.txtScrapYardSpecialization.setText(obj.getSpecialization());
        holder.rateScrapYardButton.setText(obj.getRating() + "/5");
        currentRating = obj.getRating();

        holder.locationScrapYardButton.setOnClickListener(v -> {
            Intent i = new Intent(context, MapsLocationActivity.class);
            i.putExtra("latitude", obj.getLatitude());
            i.putExtra("longitude", obj.getLongitude());
            context.startActivity(i);
        });

        holder.callScrapYardButton.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:" + obj.getPhone());
            Intent i = new Intent(Intent.ACTION_DIAL, number);
            context.startActivity(i);
        });

        holder.rateScrapYardButton.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View ratingDialogView = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null);
            final RatingBar ratingBar = ratingDialogView.findViewById(R.id.rating_bar);
            final AppCompatButton submitButton = ratingDialogView.findViewById(R.id.submit_button);
            final AppCompatButton cancelButton = ratingDialogView.findViewById(R.id.cancel_button);
            ratingBar.setRating(currentRating);
            builder.setView(ratingDialogView);
            submitButton.setOnClickListener(v1 -> {
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
                        // Handle error
                    }
                });
            });
            cancelButton.setOnClickListener(v12 -> dismissDialog());
            ratingDialog = builder.create();
            ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ratingDialog.show();
        });

        rowView.setOnClickListener(v -> {
            Intent i = new Intent(context, ProfileViewActivity.class);
            i.putExtra("id", obj.getScrapyard_id());
            i.putExtra("user_type", obj.getAccount_type());
            context.startActivity(i);
        });

        return rowView;
    }

    private void dismissDialog() {
        if (ratingDialog != null && ratingDialog.isShowing()) {
            ratingDialog.dismiss();
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<ScrapyardModel> list = data;
            int count = list.size();
            final ArrayList<ScrapyardModel> nlist = new ArrayList<>(count);
            ScrapyardModel filterableModel;
            for (int i = 0; i < count; i++) {
                filterableModel = list.get(i);
                if (filterableModel.getName().toLowerCase().contains(filterString) ||
                        filterableModel.getSpecialization().toLowerCase().contains(filterString)) {
                    nlist.add(filterableModel);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<ScrapyardModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
