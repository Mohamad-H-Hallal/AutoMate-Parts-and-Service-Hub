package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Activities.PartDetailsActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class PartsAdapter extends BaseAdapter implements Filterable {

    private List<PartModel> data;
    private List<PartModel> filteredData;
    private ArrayList<String> imagePath;
    private Context context;
    private LayoutInflater inflater;
    private ItemFilter mFilter = new ItemFilter();

    public PartsAdapter(Context context, List<PartModel> data, ArrayList<String> imagePath) {
        this.context = context;
        this.data = data;
        this.filteredData = data;
        this.imagePath = imagePath;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView partsImageView;
        TextView txtPartName, txtMake, txtYear, txtModel, txtCategory, txtPrice, txtNegotiable, txtCondition;
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
            rowView = inflater.inflate(R.layout.row_parts, parent, false);
            holder.partsImageView = rowView.findViewById(R.id.partImageView);
            holder.txtPartName = rowView.findViewById(R.id.txtPartName);
            holder.txtMake = rowView.findViewById(R.id.txtMake);
            holder.txtYear = rowView.findViewById(R.id.txtYear);
            holder.txtModel = rowView.findViewById(R.id.txtModel);
            holder.txtCategory = rowView.findViewById(R.id.txtCategory);
            holder.txtPrice = rowView.findViewById(R.id.txtPrice);
            holder.txtNegotiable = rowView.findViewById(R.id.txtNegotiable);
            holder.txtCondition = rowView.findViewById(R.id.txtCondition);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        PartModel part = filteredData.get(position);
        int id = part.getId();
        String[] categoryArray = part.getCategory().split("-");
        String[] yearArray = part.getYear().split("-");
        String[] conditionArray = part.getPart_condition().split("-");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedLanguage = preferences.getString("selected_language", "en");
        if (selectedLanguage.equals("en")) {
            holder.txtCategory.setText(categoryArray[0]);
            holder.txtCondition.setText(conditionArray[0]);
            holder.txtYear.setText(yearArray[0]);
        } else if (selectedLanguage.equals("ar")) {
            holder.txtCategory.setText(categoryArray[1]);
            holder.txtCondition.setText(conditionArray[1]);
            holder.txtYear.setText(yearArray[1]);
        }

        holder.txtMake.setText(part.getMake());
        holder.txtModel.setText(part.getModel());
        holder.txtPrice.setText("USD " + part.getPrice());

        holder.txtPartName.setText(part.getName());
        if (part.isNegotiable()) {
            holder.txtNegotiable.setText(R.string.negotiable);
        } else {
            holder.txtNegotiable.setText("");
        }

        Glide.with(context).load(Parts_IMAGES_DIR + imagePath.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.gear_def_icon)
                .into(holder.partsImageView);

        rowView.setOnClickListener(v -> {
            Intent i = new Intent(context, PartDetailsActivity.class);
            i.putExtra("part_id", String.valueOf(id));
            context.startActivity(i);
        });

        return rowView;
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
            final List<PartModel> list = data;
            int count = list.size();
            final ArrayList<PartModel> nlist = new ArrayList<>(count);
            String nego = context.getResources().getString(R.string.negotiable);
            PartModel filterableModel;
            for (int i = 0; i < count; i++) {
                filterableModel = list.get(i);
                if (filterableModel.getName().toLowerCase().contains(filterString) ||
                        filterableModel.getCategory().toLowerCase().contains(filterString) ||
                        filterableModel.getMake().toLowerCase().contains(filterString) ||
                        filterableModel.getModel().toLowerCase().contains(filterString)||
                        String.valueOf(filterableModel.getYear()).toLowerCase().contains(filterString) ||
                        String.valueOf(filterableModel.getPrice()).toLowerCase().contains(filterString) ||
                        (filterableModel.isNegotiable() ? nego : "").toLowerCase().contains(filterString) ||
                        filterableModel.getPart_condition().toLowerCase().contains(filterString)) {
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
            filteredData = (ArrayList<PartModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
