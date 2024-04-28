package com.example.project;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
    private ImageButton edit;
    private AppCompatButton manageparts;
    private ImageButton setting;

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

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        manageparts = v.findViewById(R.id.manageparts);
        edit = v.findViewById(R.id.edit);
        setting = v.findViewById(R.id.setting);
        name.setEnabled(false);
        emailtext.setEnabled(false);
        phonetext.setEnabled(false);
        specializationtext.setEnabled(false);
        editing = false;


    }

    private void setActions(View v) {
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });
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
                if (editing) {
                    edit.setImageResource(R.drawable.save_icon);
                    name.setEnabled(true);
                    emailtext.setEnabled(true);
                    phonetext.setEnabled(true);
                    specializationtext.setEnabled(true);
                    name.setTextColor(getResources().getColor(R.color.colorPrimary));
                    emailtext.setTextColor(getResources().getColor(R.color.colorPrimary));
                    phonetext.setTextColor(getResources().getColor(R.color.colorPrimary));
                    specializationtext.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Toast.makeText(getContext(), "Edit Mode", Toast.LENGTH_SHORT).show();

                } else {
                    edit.setImageResource(R.drawable.edit_icon);
                    name.setEnabled(false);
                    emailtext.setEnabled(false);
                    phonetext.setEnabled(false);
                    specializationtext.setEnabled(false);
                    name.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    emailtext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    phonetext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    specializationtext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
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
    }

    private void showImagePickerDialog() {
        String[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        captureImage();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
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

//    private boolean checkPermissions() {
//        int cameraPermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA);
//        int storagePermission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED;
//    }
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }not used for now

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                showImagePickerDialog();
//            } else {
//                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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
                    }else {
                        Toast.makeText(getContext(), "Error retrieving location data", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
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