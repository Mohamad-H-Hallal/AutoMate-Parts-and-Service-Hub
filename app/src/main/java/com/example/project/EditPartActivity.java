package com.example.project;

import static com.example.project.Configuration.IP;
import static com.example.project.GetImagePath.getRealPath;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.FileUpload.ImageUploaderClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditPartActivity extends BaseActivity implements ImageEditAdapter.OnImageRemoveListener {
    ViewPager horizontalScrollView;
    CustomMapView miniMapView;
    ImageButton back;
    ShapeableImageView location;
    CardView editpartdefault;
    TextView phone;
    ArrayList<String> imageListfromdb;
    ArrayList<String> imageListfromuser;
    ImageEditAdapter adapter;
    TextView add;
    private String typeOfeachImage = "";
    private String nameOfeachImage = "";
    private ArrayList<Uri> uriImage;
    private double latitude = 33;
    private double longitude = 35;
    private static final int GALLERY_REQUEST_CODE = 110;
    private static final int CAMERA_REQUEST_CODE = 120;
    private static final int MAP_REQUEST_CODE = 130;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_part);
        Intent i = getIntent();
        String part = i.getStringExtra("id");
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        horizontalScrollView = findViewById(R.id.e_horizontalScrollView);
        miniMapView = findViewById(R.id.e_miniMapView);
        back = findViewById(R.id.back_arrow8);
        location = findViewById(R.id.e_location_part_click);
        phone = findViewById(R.id.e_phone_detailtxt);
        add = findViewById(R.id.e_add_image);
        editpartdefault = findViewById(R.id.editpartdefault);
        uriImage=new ArrayList<Uri>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }
        imageListfromdb = new ArrayList<>();
        imageListfromuser=new ArrayList<>();
        adapter =new ImageEditAdapter(this,imageListfromdb,imageListfromuser,this);
        horizontalScrollView.setAdapter(adapter);

        miniMapView.onCreate(savedInstanceState);
        miniMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Customize the map as needed
                LatLng location = new LatLng(33, 35);
                googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MapsLocationActivity.class);
                i.putExtra("latitude",33);
                i.putExtra("longitude",35);
                startActivityForResult(i, MAP_REQUEST_CODE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    private void showImagePickerDialog() {
        String[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(cameraIntent, "Select Image Source");
        startActivityForResult(chooser, CAMERA_REQUEST_CODE);}
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooser = Intent.createChooser(cameraIntent, "Select Image Source");
                startActivityForResult(chooser, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void addImage(String imageRes) {
        imageListfromuser.add(imageRes);
        adapter.notifyDataSetChanged();
        if(!imageListfromuser.isEmpty() || !imageListfromdb.isEmpty()) {
            editpartdefault.setVisibility(View.GONE);
        }
    }

    public void deleteImage(int position) {
        if (position < imageListfromdb.size()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_image.php?name=" + imageListfromdb.get(position), response -> {
                Toast.makeText(EditPartActivity.this, response.trim(), Toast.LENGTH_SHORT).show();
                // Remove the image path from the list after successful deletion
                imageListfromdb.remove(position);
                horizontalScrollView.setAdapter(new ImageEditAdapter(this, imageListfromdb, imageListfromuser, this));
                // Check if both lists are empty and show the default view if necessary
                if (imageListfromuser.isEmpty() && imageListfromdb.isEmpty()) {
                    editpartdefault.setVisibility(View.VISIBLE);
                }
            }, error -> {
                // Error handling
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            // If the image is from the user's list, remove it from the user's list
            int adjustedPosition = position - imageListfromdb.size();
            imageListfromuser.remove(adjustedPosition);
            uriImage.remove(adjustedPosition);
            horizontalScrollView.setAdapter(new ImageEditAdapter(this, imageListfromdb, imageListfromuser, this));
            // Check if both lists are empty and show the default view if necessary
            if (imageListfromuser.isEmpty() && imageListfromdb.isEmpty()) {
                editpartdefault.setVisibility(View.VISIBLE);
            }
        }
    }



    @Override
    public void onImageRemoved(int position) {
        deleteImage(position);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    if (data != null && data.getData() != null) {
                        addImage(data.getData().toString());
                        uriImage.add(data.getData());
                    } else if (data != null && data.getClipData() != null) {
                        // Handle multiple image selection
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            addImage(uri.toString());
                            uriImage.add(uri);
                        }
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        if (imageBitmap != null) {
                            // Save the captured image to a file in external storage
                            File imageFile = saveImageToFile(imageBitmap);
                            if (imageFile != null) {
                                String imageUrl = Uri.fromFile(imageFile).toString();
                                addImage(imageUrl);
                                uriImage.add(Uri.fromFile(imageFile));
                            } else {
                                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "Failed to get image from camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error capturing image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MAP_REQUEST_CODE:
                    if (data != null && data.getExtras() != null && data.getExtras().containsKey("data")) {
                        Bundle bundle = data.getExtras();
                        latitude = bundle.getDouble("latitude");
                        longitude = bundle.getDouble("longitude");
                    } else {
                        Toast.makeText(this, "Error retrieving location data", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }
    private void uploadImages(ArrayList<Uri> imagesuri){
        for (int i = 0; i < imagesuri.size(); i++) {
            String filePath=null;

            if (Objects.equals(imagesuri.get(i).getScheme(), "content")) {
                filePath = getRealPath(this, imagesuri.get(i));
            } else if (Objects.equals(imagesuri.get(i).getScheme(), "file")) {
                filePath = imagesuri.get(i).getPath();
            }
            ContentResolver cr = this.getContentResolver();
            typeOfeachImage = cr.getType(imagesuri.get(i));
            if (typeOfeachImage != null) {
                nameOfeachImage = "icon_" + System.currentTimeMillis() + "." + typeOfeachImage.replace("image/", "");
            } else {
                nameOfeachImage = "icon_" + System.currentTimeMillis() + ".jpg";
            }
            ImageUploaderClass.uploadImage(filePath, nameOfeachImage, "images/parts", new ImageUploaderClass.onSuccessfulTask() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String error) {
                }
            });}
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
    // Remember to manage the lifecycle of the MapView
    @Override
    protected void onResume() {
        super.onResume();
        miniMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        miniMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        miniMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        miniMapView.onLowMemory();
    }

}