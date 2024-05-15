package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.USER_IMAGES_DIR;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Controller.MechanicController;
import com.example.project.Controller.ScrapyardController;
import com.example.project.Controller.UserController;
import com.example.project.Controller.UserData;
import com.example.project.Model.MechanicModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileViewActivity extends BaseActivity {
    RatingBar bar;
    ImageButton back;
    TextView location;
    AppCompatButton partsview;
    AppCompatButton rate;
    float currentRating;
    private AlertDialog ratingDialog;
    UserController controller;
    int id;
    String user_type;
    double longitude=35,latitude=33;
    private TextView v_name,v_emailtext, v_phone,v_phonetext,v_specialization,v_specializationtext,v_yearofxp,v_yearofxptext,v_biography,v_biographytext;
    private ShapeableImageView v_profile_image;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        bar = findViewById(R.id.v_rating_bar);
        bar.setRating((float) 3.5);
        back = findViewById(R.id.back_arrow6);
        location = findViewById(R.id.v_locationtext);
        rate = findViewById(R.id.v_rating);
        partsview = findViewById(R.id.v_manageparts);
        v_name = findViewById(R.id.v_name);
        v_emailtext=findViewById(R.id.v_emailtext);
        v_phone=findViewById(R.id.v_phone);
        v_phonetext=findViewById(R.id.v_phonetext);
        v_specialization=findViewById(R.id.v_specialization);
        v_specializationtext=findViewById(R.id.v_specializationtext);
        v_yearofxp=findViewById(R.id.v_yearofxp);
        v_yearofxptext=findViewById(R.id.v_yearofxptext);
        v_biography=findViewById(R.id.v_bio);
        v_biographytext=findViewById(R.id.v_biotext);
        v_profile_image=findViewById(R.id.v_profile_image);
        controller = new UserController();

        Intent i =getIntent();
        id = i.getIntExtra("id",0);
        user_type=i.getStringExtra("user_type");
        v_yearofxp.setVisibility(View.GONE);
        v_yearofxptext.setVisibility(View.GONE);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(ProfileViewActivity.this, MapsLocationActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivity(i);

            }

        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewActivity.this);
                View ratingDialogView = LayoutInflater.from(ProfileViewActivity.this).inflate(R.layout.rating_dialog, null);
                final RatingBar ratingBar = ratingDialogView.findViewById(R.id.rating_bar);
                final AppCompatButton submitButton = ratingDialogView.findViewById(R.id.submit_button);
                final AppCompatButton cancelButton = ratingDialogView.findViewById(R.id.cancel_button);



                ratingBar.setRating(currentRating);
                builder.setView(ratingDialogView);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentRating = ratingBar.getRating();
                        if(user_type.equals("Scrap-Yard Vendor")){
                            ScrapyardController scrapcontroller = new ScrapyardController();
                            scrapcontroller.submitRating(ProfileViewActivity.this, ratingBar.getRating(), UserData.getId(), id, new ScrapyardController.ScrapyardRateListener() {
                                @Override
                                public void onrateScrapyardDataReceived(Float rate) {
                                    bar.setRating(rate);

                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        } else if (user_type.equals("Mechanic")) {
                            MechanicController mechanicController = new MechanicController();
                            mechanicController.submitRating(ProfileViewActivity.this, ratingBar.getRating(), UserData.getId(), id, new MechanicController.MechanicRateListener() {
                                @Override
                                public void onrateMechanicDataReceived(Float rate) {
                                    bar.setRating(rate);
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });

                        }
                        Toast.makeText(ProfileViewActivity.this, "Rating submitted: " + currentRating, Toast.LENGTH_SHORT).show();
                        if (ratingDialog != null && ratingDialog.isShowing()) {
                            ratingDialog.dismiss();
                        }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ratingDialog != null && ratingDialog.isShowing()) {
                            ratingDialog.dismiss();
                        }
                    }
                });
                ratingDialog = builder.create();
                ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingDialog.show();
            }
        });
        if (user_type.equals("Scrap-Yard Vendor")) {
            partsview.setVisibility(View.VISIBLE);
            rate.setVisibility(View.VISIBLE);
            Log.d("test",String.valueOf(id));
            controller.getScrapyardData(this, id, new UserController.ScrapyardDataListener() {
                @Override
                public void onScrapyardDataReceived(ScrapyardModel user) {

                    v_name.setText(user.getName());
                    v_emailtext.setText(user.getEmail());

                    v_phonetext.setText(user.getPhone());

                    v_specializationtext.setText(user.getSpecialization());
                    bar.setRating(user.getRating());

                    if (!user.getBiography().equals("NULL")) {
                        v_biographytext.setText(user.getBiography());
                    }

                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    Glide.with(ProfileViewActivity.this).load(USER_IMAGES_DIR + user.getIcon())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.profile_def_icon)
                            .into(v_profile_image);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d("test", error.getMessage());
                }
            });
            partsview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProfileViewActivity.this, PartsCategoriesActivity.class);
                    i.putExtra("viewer", 1);
                    i.putExtra("id",id);
                    startActivity(i);
                }
            });

        }else if(user_type.equals("Mechanic")){
            partsview.setVisibility(View.GONE);
            controller.getMechanicData(this, id, new UserController.MechanicDataListener() {
                @Override
                public void onMechanicDataReceived(MechanicModel user) {
                    v_name.setText(user.getName());
                    v_emailtext.setText(user.getEmail());
                    v_phonetext.setText(user.getPhone());
                    v_specializationtext.setText(user.getSpecialization());
                    bar.setRating(user.getRating());
                    if (!user.getBiography().equals("NULL")) {
                        v_biographytext.setText(user.getBiography());
                    }
                    v_yearofxp.setVisibility(View.VISIBLE);
                    v_yearofxptext.setVisibility(View.VISIBLE);
                    v_yearofxptext.setText(String.valueOf(user.getYear_of_experience()));

                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    Glide.with(ProfileViewActivity.this).load(USER_IMAGES_DIR + user.getIcon())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.profile_def_icon)
                            .into(v_profile_image);
                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }
    }


}