package com.example.project.View.Activities;

import static com.example.project.Controller.GetImagePath.getRealPath;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.project.Controller.UserController;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.R;
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
    TextInputLayout textInputMobile, textInputSpecialization, textInputName, textInputEmail, textInputPassword;
    private AlertDialog Dialog;
    TextView locationText;
    EditText editTextName, editTextEmail, editTextPassword, editTextMobile, editTextSpecialization;
    private Uri uriImage = null;
    private String nameOfImage = "";
    private String typeOfImage = "";
    private ShapeableImageView profilePictureView;
    double latitude;
    double longitude;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        sp = findViewById(R.id.spinnerAccountType);
        textInputMobile = findViewById(R.id.textInputMobile);
        textInputSpecialization = findViewById(R.id.textInputSpecialization);
        regbtn = findViewById(R.id.registerButton);
        textInputName = findViewById(R.id.textInputName);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        editTextName = textInputName.getEditText();
        editTextEmail = textInputEmail.getEditText();
        editTextPassword = textInputPassword.getEditText();
        editTextMobile = textInputMobile.getEditText();
        editTextSpecialization = textInputSpecialization.getEditText();
        locationText = findViewById(R.id.locationText);
        profilePictureView = findViewById(R.id.profileImageView);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                switch (pos) {
                    case 0:
                    case 1:
                        textInputMobile.setVisibility(View.GONE);
                        textInputSpecialization.setVisibility(View.GONE);

                        break;
                    case 2:
                    case 3:
                        textInputMobile.setVisibility(View.VISIBLE);
                        textInputSpecialization.setVisibility(View.VISIBLE);

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
                if (uriImage != null) {
                    String name = editTextName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String accountType = sp.getSelectedItem().toString();
                    String icon = nameOfImage;
                    String phone = editTextMobile.getText().toString();
                    String specialization = editTextSpecialization.getText().toString();
                    String strLatitude = String.valueOf(latitude);
                    String strLongitude = String.valueOf(longitude);
                    if (!(name.isEmpty() || email.isEmpty() || password.isEmpty() || icon.isEmpty() || strLatitude.isEmpty() || strLongitude.isEmpty())) {
                        uploadImage();
                        if (accountType.equals(sp.getItemAtPosition(1).toString())) {
                            UserController.registerUser(RegisterActivity.this, name, email, password, latitude, longitude, "General User", icon, phone, specialization, new UserController.RegistrationCallback() {
                                @Override
                                public void onRegistrationSuccess(String response) {
                                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onRegistrationError(String error) {
                                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (accountType.equals(sp.getItemAtPosition(2).toString())) {
                            if (!(phone.isEmpty() || specialization.isEmpty())) {
                                UserController.registerUser(RegisterActivity.this, name, email, password, latitude, longitude, "Mechanic", icon, phone, specialization, new UserController.RegistrationCallback() {
                                    @Override
                                    public void onRegistrationSuccess(String response) {
                                        if (response.equals("User registered successfully")) {
                                            showPaymentDialog();
                                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onRegistrationError(String error) {
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Enter all needed information!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (accountType.equals(sp.getItemAtPosition(3).toString())) {
                            if (!(phone.isEmpty() || specialization.isEmpty())) {
                                UserController.registerUser(RegisterActivity.this, name, email, password, latitude, longitude, "Scrap-Yard Vendor", icon, phone, specialization, new UserController.RegistrationCallback() {
                                    @Override
                                    public void onRegistrationSuccess(String response) {
                                        if (response.equals("User registered successfully")) {
                                            showPaymentDialog();
                                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onRegistrationError(String error) {
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Enter all needed information!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Choose your account type!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Enter all needed information!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Choose icon", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        View dialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.payment_def_dialog, null);
        final AppCompatButton getStartedButton = dialogView.findViewById(R.id.getStarted);
        builder.setView(dialogView);
        Dialog = builder.create();
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dialog.show();
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Dialog != null && Dialog.isShowing()) {
                    Dialog.dismiss();
                    finish();
                }
            }
        });
    }

    private void uploadImage() {
        String filePath = null;

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

            }
        });
    }


    public void onChooseImageClicked(View view) {
        showImagePickerDialog();
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View DialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.picture_dialog, null);
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
                        latitude = data.getDoubleExtra("latitude", 0);
                        longitude = data.getDoubleExtra("longitude", 0);
                        locationText.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                    } else {
                        Toast.makeText(this, "Error retrieving location data", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
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
