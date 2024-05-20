package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;
import static com.example.project.Controller.Configuration.USER_IMAGES_DIR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.example.project.Controller.MechanicController;
import com.example.project.Controller.UserData;
import com.example.project.Model.MechanicModel;
import com.example.project.R;
import com.example.project.View.Activities.MapsLocationActivity;
import com.example.project.View.Activities.ProfileViewActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MechanicAdapter extends BaseAdapter implements Filterable {

    private List<MechanicModel> data;
    private List<MechanicModel> filteredData;
    private Context context;
    private float currentRating = 0;
    private AlertDialog ratingDialog;
    private LayoutInflater inflater;
    private ItemFilter mFilter = new ItemFilter();

    public MechanicAdapter(Context context, List<MechanicModel> data) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
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
        String language = preferences.getString("selected_language", "en");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        notifyDataSetChanged(); // Notify the adapter that the data has changed
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;
        if (rowView == null) {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.row_mechanic, parent, false);
            holder.mechanicImageView = rowView.findViewById(R.id.mechanicImageView);
            holder.txtMechanicName = rowView.findViewById(R.id.txtMechanicName);
            holder.txtMechanicSpecialization = rowView.findViewById(R.id.txtMechanicSpecialization);
            holder.callMechanicButton = rowView.findViewById(R.id.callMechanicButton);
            holder.locationMechanicButton = rowView.findViewById(R.id.locationMechanicButton);
            holder.rateMechanicButton = rowView.findViewById(R.id.rateMechanicButton);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        MechanicModel mechanic = filteredData.get(position);
        Glide.with(context).load(USER_IMAGES_DIR + mechanic.getIcon())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.profile_def_icon)
                .into(holder.mechanicImageView);
        holder.txtMechanicName.setText(mechanic.getName());
        holder.txtMechanicSpecialization.setText(mechanic.getSpecialization());
        holder.rateMechanicButton.setText(mechanic.getRating() + "/5");
        currentRating = mechanic.getRating();

        holder.locationMechanicButton.setOnClickListener(v -> {
            Intent i = new Intent(context, MapsLocationActivity.class);
            i.putExtra("latitude", mechanic.getLatitude());
            i.putExtra("longitude", mechanic.getLongitude());
            i.putExtra("name",mechanic.getName());
            context.startActivity(i);
        });

        holder.callMechanicButton.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:" + mechanic.getPhone());
            Intent i = new Intent(Intent.ACTION_DIAL, number);
            context.startActivity(i);
        });

        holder.rateMechanicButton.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View ratingDialogView = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null);
            final RatingBar ratingBar = ratingDialogView.findViewById(R.id.rating_bar);
            final AppCompatButton submitButton = ratingDialogView.findViewById(R.id.submit_button);
            final AppCompatButton cancelButton = ratingDialogView.findViewById(R.id.cancel_button);
            ratingBar.setRating(currentRating);
            builder.setView(ratingDialogView);
            submitButton.setOnClickListener(v1 -> {
                currentRating = ratingBar.getRating();
                MechanicController controller = new MechanicController();

                controller.submitRating(context, currentRating, UserData.getId(), mechanic.getMechanic_id(), new MechanicController.MechanicRateListener() {
                    @Override
                    public void onrateMechanicDataReceived(Float rate) {
                        holder.rateMechanicButton.setText(rate + "/5");
                        Toast.makeText(context, "Rating submitted: " + currentRating, Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d("error", error.getMessage());
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
            i.putExtra("id", mechanic.getMechanic_id());
            i.putExtra("user_type", mechanic.getAccount_type());
            context.startActivity(i);
        });

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private void dismissDialog() {
        if (ratingDialog != null && ratingDialog.isShowing()) {
            ratingDialog.dismiss();
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<MechanicModel> list = data;
            int count = list.size();
            final ArrayList<MechanicModel> nlist = new ArrayList<>(count);

            MechanicModel filterableModel;
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
            filteredData = (ArrayList<MechanicModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
