package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Activities.PartDetailsActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartsAdapter extends BaseAdapter {

    List<PartModel> data;
    ArrayList<String> image_path;
    Context context;
    LayoutInflater inflater = null;

    public PartsAdapter(Context context, List<PartModel> data, ArrayList<String> image_path) {
        this.context = context;
        this.data = data;
        this.image_path=image_path;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        ShapeableImageView partsImageView;
        TextView txtPartName, txtMake, txtYear, txtModel, txtCategory, txtPrice, txtNegotiable, txtCondition;
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

        PartModel part = data.get(position);
        int id = part.getId();
        holder.txtCategory.setText(part.getCategory());
        holder.txtMake.setText(part.getMake());
        holder.txtModel.setText(part.getModel());
        holder.txtPrice.setText("USD "+ part.getPrice());
        holder.txtCondition.setText(part.getPart_condition());
        holder.txtYear.setText(Integer.toString(part.getYear()));
        holder.txtPartName.setText(part.getName());
        if(part.isNegotiable()){
        holder.txtNegotiable.setText(R.string.negotiable);}
        else{holder.txtNegotiable.setText("");}

        Glide.with(context).load(Parts_IMAGES_DIR + image_path.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.gear_def_icon)
                .into(holder.partsImageView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PartDetailsActivity.class);
                i.putExtra("part_id",String.valueOf(id));
                context.startActivity(i);
            }
        });

        return rowView;
    }


}
