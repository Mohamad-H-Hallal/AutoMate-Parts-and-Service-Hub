package com.example.project.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.View.Activities.EditPartActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManagePartsAdapter extends BaseAdapter {

    Context context;
    JSONArray data;
    LayoutInflater inflater;

    public ManagePartsAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        ShapeableImageView partsImageView;
        TextView txtPartName, txtMake, txtYear, txtModel, txtCategory, txtPrice, txtNegotiable, txtCondition;
        ShapeableImageView delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ManagePartsAdapter.Holder holder = new ManagePartsAdapter.Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row_manage_parts,null);
        holder.partsImageView = rowView.findViewById(R.id.partsManageImageView);
        holder.txtPartName = rowView.findViewById(R.id.txtManagePartName);
        holder.txtMake = rowView.findViewById(R.id.txtManageMake);
        holder.txtYear = rowView.findViewById(R.id.txtManageYear);
        holder.txtModel = rowView.findViewById(R.id.txtManageModel);
        holder.txtCategory = rowView.findViewById(R.id.txtManageCategory);
        holder.txtPrice = rowView.findViewById(R.id.txtManagePrice);
        holder.txtNegotiable = rowView.findViewById(R.id.txtManageNegotiable);
        holder.txtCondition = rowView.findViewById(R.id.txtManageCondition);
        holder.delete = rowView.findViewById(R.id.icon_delete);
        JSONObject obj = data.optJSONObject(position);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPartActivity.class);
                intent.putExtra("id", obj.optString("id"));
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete from databse
                notifyDataSetChanged();
            }
        });
            return null;
    }
}