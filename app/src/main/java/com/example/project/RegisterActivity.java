package com.example.project;

import static com.example.project.GetImagePath.getRealPath;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.project.FileUpload.ImageUploaderClass;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends BaseActivity {

    private static final int MAP_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    Spinner sp;
    AppCompatButton regbtn;
    TextInputLayout til1, til2;

    TextView locationText;

    private Uri uriImage = null;
    private String nameOfImage = "";
    private String typeOfImage = "";
    private ShapeableImageView profilePictureView;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        sp = findViewById(R.id.spinnerAccountType);
        til1 = findViewById(R.id.textInputMobile);
        til2 = findViewById(R.id.textInputSpecialization);
        regbtn = findViewById(R.id.registerButton);

        locationText = findViewById(R.id.locationText);
        profilePictureView = findViewById(R.id.profileImageView);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                switch (pos) {
                    case 0:
                    case 1:
                        til1.setVisibility(View.GONE);
                        til2.setVisibility(View.GONE);

                        break;
                    case 2:
                    case 3:
                        til1.setVisibility(View.VISIBLE);
                        til2.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriImage != null){

                    uploadImage();
//                insertUser();


                }else {
                    Toast.makeText(RegisterActivity.this, "Choose icon", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadImage(){
        String filePath=null;

        if (Objects.equals(uriImage.getScheme(), "content")) {
            filePath = getRealPath(this, uriImage);
        } else if (Objects.equals(uriImage.getScheme(), "file")) {
            filePath = uriImage.getPath();
        }

        ImageUploaderClass.uploadImage(filePath, nameOfImage, "images/users", new ImageUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {
            Log.d("error",error);
            }
        });
    }


    public void onChooseImageClicked(View view) {
        showImagePickerDialog();
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
            startActivityForResult(chooser, CAMERA_REQUEST_CODE);
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            activityResultLauncher.launch(intent);
        }
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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent chooser = Intent.createChooser(galleryIntent, "Select Image Source");
                startActivityForResult(chooser, GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:

                    if (data.getData() != null) {
                        uriImage = data.getData();
                        ContentResolver cr = this.getContentResolver();
                        typeOfImage = cr.getType(uriImage);
                        Glide.with(this).load(uriImage).into(profilePictureView);
                        nameOfImage = "icon_" + System.currentTimeMillis() + "." + typeOfImage.replace("image/", "");
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
                                uriImage = Uri.fromFile(imageFile);
                                Glide.with(this).load(uriImage).into(profilePictureView);
                                nameOfImage = "icon_" + System.currentTimeMillis() + ".jpg";
                            } else {
                                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Failed to get image from camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error capturing image", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MAP_REQUEST_CODE:
                    if (data != null) {
                        double latitude = data.getDoubleExtra("latitude", 0);
                        double longitude = data.getDoubleExtra("longitude", 0);
                        locationText.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                    } else {
                        Toast.makeText(this, "Error retrieving location data", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    private void setProfilePicture(Bitmap profileImage) {
        profilePictureView = findViewById(R.id.profileImageView);
        profilePictureView.setImageBitmap(profileImage);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onSelectLocationClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);
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
