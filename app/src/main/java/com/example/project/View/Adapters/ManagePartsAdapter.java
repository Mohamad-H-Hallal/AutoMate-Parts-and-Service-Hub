package com.example.project.View.Adapters;

import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Controller.PartController;
import com.example.project.Model.PartModel;
import com.example.project.R;
import com.example.project.View.Activities.EditPartActivity;
import com.example.project.View.Activities.PartDetailsActivity;
import com.example.project.View.Activities.SettingActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManagePartsAdapter extends BaseAdapter {

    Context context;
    List<PartModel> data;
    ArrayList<String> image_path;
    LayoutInflater inflater;

    public ManagePartsAdapter(Context context, List<PartModel> data, ArrayList<String> image_path) {
        this.context = context;
        this.data = data;
        this.image_path=image_path;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class Holder {
        ShapeableImageView partsImageView;
        TextView txtPartName, txtMake, txtYear, txtModel, txtCategory, txtPrice, txtNegotiable, txtCondition;
        ImageView delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ManagePartsAdapter.Holder holder = new ManagePartsAdapter.Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row_manage_parts,null);
        holder.partsImageView = rowView.findViewById(R.id.partsManageImageView);
        holder.txtPartName = rowView.findViewById(R.id.txtManageName);
        holder.txtMake = rowView.findViewById(R.id.txtManageMake);
        holder.txtYear = rowView.findViewById(R.id.txtManageYear);
        holder.txtModel = rowView.findViewById(R.id.txtManageModel);
        holder.txtCategory = rowView.findViewById(R.id.txtManageCategory);
        holder.txtPrice = rowView.findViewById(R.id.txtManagePrice);
        holder.txtNegotiable = rowView.findViewById(R.id.txtManageNegotiable);
        holder.txtCondition = rowView.findViewById(R.id.txtManageCondition);
        holder.delete = rowView.findViewById(R.id.deleteIcon);

        PartModel part = data.get(position);
        int id = part.getId();

        String[] categoryArray = part.getCategory().split("-");
        String[] yearArray = part.getYear().split("-");
        String[] conditionArray = part.getPart_condition().split("-");
        String[] subcategoryArray = part.getSubcategory().split("-");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedLanguage = preferences.getString("selected_language", "");
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
        holder.txtPrice.setText("USD "+ part.getPrice());
        holder.txtPartName.setText(part.getName());
        if(part.isNegotiable()){
            holder.txtNegotiable.setText(R.string.negotiable);}
        else
            holder.txtNegotiable.setText("");
        Glide.with(context).load(Parts_IMAGES_DIR + image_path.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.gear_def_icon)
                .into(holder.partsImageView);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditPartActivity.class);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String selectedLanguage = preferences.getString("selected_language", "");
                if (selectedLanguage.equals("en")) {
                    i.putExtra("category",categoryArray[0]);
                    i.putExtra("subcategory",subcategoryArray[0]);
                } else if (selectedLanguage.equals("ar")) {
                    i.putExtra("category",categoryArray[1]);
                    i.putExtra("subcategory",subcategoryArray[1]);
                }
                i.putExtra("part_id",String.valueOf(id));
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int partId = part.getId();
                PartController partController = new PartController();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View DialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                final AppCompatButton yesButton = DialogView.findViewById(R.id.del_yes_button);
                final AppCompatButton noButton = DialogView.findViewById(R.id.del_no_button);
                builder.setView(DialogView);
                final AlertDialog Dialog = builder.create();
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partController.deletePart(context, partId, new PartController.PartDeleteListener() {
                            @Override
                            public void onDeleteSuccess(String message) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                data.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onDeleteError(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Dialog.show();
            }
        });
            return rowView;
    }
}
