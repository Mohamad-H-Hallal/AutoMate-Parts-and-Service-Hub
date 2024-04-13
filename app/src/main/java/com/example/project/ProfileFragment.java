package com.example.project;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

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

    private Uri uriImage = null;
    private String typeOfImage = "";
    private String nameOfImage = "";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private double latitude = 33;
    private double longitude = 35;


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
        profile_image=v.findViewById(R.id.profile_image);
        reset_pass= v.findViewById(R.id.reset_pass);
        logout=v.findViewById(R.id.logout);
        name=v.findViewById(R.id.name);
        locationtext=v.findViewById(R.id.locationtext);
        emailtext=v.findViewById(R.id.emailtext);
        phone=v.findViewById(R.id.phone);
        phonetext=v.findViewById(R.id.phonetext);
        specialization=v.findViewById(R.id.specialization);
        specializationtext=v.findViewById(R.id.specializationtext);
        edit=v.findViewById(R.id.edit);
        name.setEnabled(false);
        emailtext.setEnabled(false);
        phonetext.setEnabled(false);
        phonetext.setEnabled(false);
        specializationtext.setEnabled(false);
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
            Intent i = new Intent(getContext(),MapsLocationActivity.class);
            startActivity(i);
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

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_CODE) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            showImagePickerDialog();
        } else {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
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

                            profile_image.setImageBitmap(imageBitmap);
                        } else {
                            Toast.makeText(getContext(), "Failed to get image from camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error capturing image", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }




}