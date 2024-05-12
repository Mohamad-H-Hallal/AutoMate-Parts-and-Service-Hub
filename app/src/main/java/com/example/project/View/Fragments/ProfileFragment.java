package com.example.project.View.Fragments;


import static com.example.project.Controller.Configuration.Parts_IMAGES_DIR;
import static com.example.project.Controller.GetImagePath.getRealPath;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Controller.UserController;
import com.example.project.Controller.UserData;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.Model.MechanicModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.Model.UserModel;
import com.example.project.R;
import com.example.project.View.Activities.EditLocationActivity;
import com.example.project.View.Activities.MapsLocationActivity;
import com.example.project.View.Activities.PartsCategoriesActivity;
import com.example.project.View.Activities.PaymentActivity;
import com.example.project.View.Activities.SettingActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends BaseFragment {

    private static final int PAYMENT_REQUEST_CODE = 17;
    private ShapeableImageView profile_image;
    private ImageButton reset_pass;
    private ImageButton logout;
    private TextView name;
    private TextView locationtext;
    private TextView emailtext;
    private TextView phone;
    private TextView phonetext;
    private TextView specialization;
    private TextView specializationtext;
    private TextView subscription;
    private TextView subscriptiontext;
    private TextView yearofxp;
    private TextView yearofxptext;
    private TextView biographytext;
    private TextView biography;
    private ImageButton edit;
    private ImageButton pay;
    private AppCompatButton manageparts;
    private RatingBar p_rating_bar;
    private ImageButton setting;
    private AlertDialog Dialog;

    private Uri uriImage = null;
    private String typeOfImage = "";
    private String nameOfImage = "";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int MAP_REQUEST_CODE = 103;
    private double latitude = 33;
    private double longitude = 35;
    private Boolean editing;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialize(view);
        setActions(view);

    }

    private void setInitialize(View v) {
        profile_image = v.findViewById(R.id.profile_image);
        reset_pass = v.findViewById(R.id.reset_pass);
        logout = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.name);
        locationtext = v.findViewById(R.id.locationtext);
        emailtext = v.findViewById(R.id.emailtext);
        phone = v.findViewById(R.id.phone);
        phonetext = v.findViewById(R.id.phonetext);
        specialization = v.findViewById(R.id.specialization);
        specializationtext = v.findViewById(R.id.specializationtext);
        subscription = v.findViewById(R.id.subscription);
        subscriptiontext = v.findViewById(R.id.subscriptiontext);
        yearofxp = v.findViewById(R.id.yearofxp);
        yearofxptext = v.findViewById(R.id.yearofxptext);
        biography = v.findViewById(R.id.biography);
        biographytext = v.findViewById(R.id.biographytext);
        manageparts = v.findViewById(R.id.manageparts);
        edit = v.findViewById(R.id.edit);
        p_rating_bar=v.findViewById(R.id.p_rating_bar);

        setting = v.findViewById(R.id.setting);
        pay = v.findViewById(R.id.pay);
        name.setEnabled(false);
        emailtext.setEnabled(false);
        phonetext.setEnabled(false);
        specializationtext.setEnabled(false);
        editing = false;
        UserController usercontroller = new UserController();
        UserData userdata = new UserData(requireContext());
        if(userdata.getAccountType().equals("General User")){
            usercontroller.getUserData(requireContext(), userdata.getId(), new UserController.UserDataListener() {
                @Override
                public void onUserDataReceived(UserModel user) {
                    name.setText(user.getName());
                    emailtext.setText(user.getEmail());
                    phone.setVisibility(View.GONE);
                    phonetext.setVisibility(View.GONE);
                    specialization.setVisibility(View.GONE);
                    specializationtext.setVisibility(View.GONE);
                    p_rating_bar.setVisibility(View.GONE);
                    yearofxp.setVisibility(View.GONE);
                    yearofxptext.setVisibility(View.GONE);
                    biographytext.setVisibility(View.GONE);
                    biography.setVisibility(View.GONE);
                    subscription.setVisibility(View.GONE);
                    subscriptiontext.setVisibility(View.GONE);
                    manageparts.setVisibility(View.GONE);
                    pay.setVisibility(View.GONE);
                    p_rating_bar.setVisibility(View.GONE);
                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    Glide.with(requireContext()).load(Parts_IMAGES_DIR + user.getIcon())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.test)
                            .into(profile_image);
                }
            
                @Override
                public void onError(VolleyError error) {
                    // Handle error
                }
            });} else if (userdata.getAccountType().equals("Mechanic")) {
            usercontroller.getMechanicData(requireContext(), userdata.getId(), new UserController.MechanicDataListener() {
                @Override
                public void onMechanicDataReceived(MechanicModel user) {
                    name.setText(user.getName());
                    emailtext.setText(user.getEmail());
                    phone.setVisibility(View.VISIBLE);
                    phonetext.setVisibility(View.VISIBLE);
                    phonetext.setText(user.getPhone());
                    specialization.setVisibility(View.VISIBLE);
                    specializationtext.setVisibility(View.VISIBLE);
                    specializationtext.setText(user.getSpecialization());
                    p_rating_bar.setVisibility(View.VISIBLE);
                    p_rating_bar.setRating(user.getRating());
                    yearofxp.setVisibility(View.VISIBLE);
                    yearofxptext.setVisibility(View.VISIBLE);
                    yearofxptext.setText(user.getYear_of_experience());
                    biographytext.setVisibility(View.VISIBLE);
                    biographytext.setText(user.getBiography());
                    biography.setVisibility(View.VISIBLE);
                    subscription.setVisibility(View.VISIBLE);
                    subscriptiontext.setVisibility(View.VISIBLE);
                    subscriptiontext.setText(user.getSubscription());
                    manageparts.setVisibility(View.GONE);
                    pay.setVisibility(View.VISIBLE);
                    p_rating_bar.setVisibility(View.VISIBLE);
                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    Glide.with(requireContext()).load(Parts_IMAGES_DIR + user.getIcon())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.test)
                            .into(profile_image);
                }

                @Override
                public void onError(VolleyError error) {
                    // Handle error
                }
            });
            
                } else if (userdata.getAccountType().equals("Scrap-Yard Vendor")) {
            usercontroller.getScrapyardData(requireContext(), userdata.getId(), new UserController.ScrapyardDataListener() {
                @Override
                public void onScrapyardDataReceived(ScrapyardModel user) {
                    name.setText(user.getName());
                    emailtext.setText(user.getEmail());
                    phone.setVisibility(View.VISIBLE);
                    phonetext.setVisibility(View.VISIBLE);
                    phonetext.setText(user.getPhone());
                    specialization.setVisibility(View.VISIBLE);
                    specializationtext.setVisibility(View.VISIBLE);
                    specializationtext.setText(user.getSpecialization());
                    p_rating_bar.setVisibility(View.VISIBLE);
                    p_rating_bar.setRating(user.getRating());
                    yearofxp.setVisibility(View.GONE);
                    yearofxptext.setVisibility(View.GONE);
                    biographytext.setVisibility(View.VISIBLE);
                    biographytext.setText(user.getBiography());
                    biography.setVisibility(View.VISIBLE);
                    subscription.setVisibility(View.VISIBLE);
                    subscriptiontext.setVisibility(View.VISIBLE);
                    subscriptiontext.setText(user.getSubscription());
                    manageparts.setVisibility(View.VISIBLE);
                    pay.setVisibility(View.VISIBLE);
                    p_rating_bar.setVisibility(View.VISIBLE);
                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    Glide.with(requireContext()).load(Parts_IMAGES_DIR + user.getIcon())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.test)
                            .into(profile_image);
                }

                @Override
                public void onError(VolleyError error) {
                    // Handle error
                }
            });
        }

    }



    private void setActions(View v) {

        locationtext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editing) {
                    Intent i = new Intent(getContext(), EditLocationActivity.class);
                    startActivityForResult(i, MAP_REQUEST_CODE);
                } else {
                    Intent i = new Intent(getContext(), MapsLocationActivity.class);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    startActivity(i);

                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editing = !editing;
                if (!editing) {
                    showSaveConfirmationDialog();
                }

                if (editing) {
                    // Toggle editing mode
                    toggleEditingMode();
                }

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing) {
                    showImagePickerDialog();
                }
            }
        });
        manageparts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PartsCategoriesActivity.class);
                startActivity(i);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SettingActivity.class);
                startActivity(i);
                requireActivity().finish();

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), PaymentActivity.class), PAYMENT_REQUEST_CODE);
            }
        });
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View DialogView = LayoutInflater.from(getContext()).inflate(R.layout.picture_dialog, null);
        final AppCompatButton takeButton = DialogView.findViewById(R.id.takePhotoButton);
        final AppCompatButton chooseButton = DialogView.findViewById(R.id.chooseGalleryButton);
        builder.setView(DialogView);
        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                if (Dialog != null && Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                if (Dialog != null && Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            }
        });
        Dialog = builder.create();
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dialog.show();
    }

    private void showSaveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.edit_mode_dialog, null);
        final AppCompatButton yesButton = dialogView.findViewById(R.id.cs_yes_button);
        final AppCompatButton noButton = dialogView.findViewById(R.id.cs_no_button);
        builder.setView(dialogView);

        // Create the dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false); // Prevent dismissal by clicking outside

        // Handle click listeners for the dialog buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriImage != null) {
                    uploadImage();
                }
                saveChanges();
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editing = !editing;
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void saveChanges() {
        // Save changes logic here
        edit.setImageResource(R.drawable.edit_icon);
        name.setEnabled(false);
        phonetext.setEnabled(false);
        specializationtext.setEnabled(false);
        name.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        phonetext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        specializationtext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    private void toggleEditingMode() {
        // Toggle editing mode
        edit.setImageResource(R.drawable.save_icon);
        name.setEnabled(true);
        phonetext.setEnabled(true);
        specializationtext.setEnabled(true);
        name.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        phonetext.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        specializationtext.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        Toast.makeText(getContext(), "Edit Mode", Toast.LENGTH_SHORT).show();
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(cameraIntent, "Select Image Source");
        startActivityForResult(chooser, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser = Intent.createChooser(galleryIntent, "Select Image Source");
        startActivityForResult(chooser, GALLERY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    if (data.getData() != null) {
                        uriImage = data.getData();
                        ContentResolver cr = getContext().getContentResolver();
                        typeOfImage = cr.getType(uriImage);
                        Glide.with(this).load(uriImage).into(profile_image);
                        nameOfImage = "icon_" + System.currentTimeMillis() + "." + typeOfImage.replace("image/", "");
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        if (imageBitmap != null) {
                            // Save the captured image to a file in external storage
                            File imageFile = saveImageToFile(imageBitmap);
                            if (imageFile != null) {

                                uriImage = Uri.fromFile(imageFile);

                                String imageUrl = uriImage.toString();
                                Glide.with(this).load(uriImage).into(profile_image);
                                nameOfImage = "icon_" + System.currentTimeMillis() + ".jpg";
                            } else {
                                Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to get image from camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error capturing image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MAP_REQUEST_CODE:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")) {
                        Bundle bundle = data.getExtras();
                        latitude = bundle.getDouble("latitude");
                        longitude = bundle.getDouble("longitude");
                    } else {
                        Toast.makeText(getContext(), "Error retrieving location data", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PAYMENT_REQUEST_CODE:
                    requireActivity().finish();

            }
        }
    }

    private void uploadImage() {
        String filePath = null;

        if (Objects.equals(uriImage.getScheme(), "content")) {
            filePath = getRealPath(getContext(), uriImage);
        } else if (Objects.equals(uriImage.getScheme(), "file")) {
            filePath = uriImage.getPath();
        }

        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/users", new ImageUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {
                Log.d("error", error);
            }
        });
    }

    private File saveImageToFile(Bitmap imageBitmap) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AutoMate");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(mediaFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return mediaFile;
    }
}