package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;
import static com.example.project.Controller.GetImagePath.getRealPath;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Controller.PartController;
import com.example.project.Controller.TwoDecimalPlacesInputFilter;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.R;
import com.example.project.View.Adapters.ImageAddAdapter;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class AddPartsActivity extends BaseActivity implements ImageAddAdapter.OnImageRemoveListener {

    private static final int CAMERA_REQUEST_CODE = 150;
    private static final int GALLERY_REQUEST_CODE = 170;

    private ImageButton back;
    private CardView addPartCardView;
    ArrayList<String> imageList;
    ArrayList<String> imagePaths;
    ImageAddAdapter adapter;
    private ViewPager addPartHorizontalScrollView;
    TextView addpartimage;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String typeOfeachImage = "";
    private String nameOfeachImage = "";
    ArrayList<Uri> uriImages;
    private AlertDialog Dialog;
    private AppCompatButton addPartButton;
    private Spinner make, model, year, category, subcategories, condition;
    private TextInputLayout textInputAddPartName, textInputAddPartPrice, textInputAddPartDescription;
    private EditText addPartName, addPartPrice, addPartDescription;
    private CheckBox addPartNegotiable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parts);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        back = findViewById(R.id.back_arrow7);
        addPartCardView = findViewById(R.id.addPartCardView);
        addpartimage = findViewById(R.id.addPartImage);
        addPartHorizontalScrollView = findViewById(R.id.addPartHorizontalScrollView);
        addPartButton = findViewById(R.id.addPartButton);
        make = findViewById(R.id.addPartMakeSpinner);
        model = findViewById(R.id.addPartModelSpinner);
        year = findViewById(R.id.addPartYearSpinner);
        category = findViewById(R.id.addPartCategorySpinner);
        subcategories = findViewById(R.id.addPartSubCategorySpinner);
        condition = findViewById(R.id.addPartConditionSpinner);
        textInputAddPartName = findViewById(R.id.textInputAddPartName);
        textInputAddPartPrice = findViewById(R.id.textInputAddPartPrice);
        textInputAddPartDescription = findViewById(R.id.textInputAddPartDescription);
        addPartName = textInputAddPartName.getEditText();
        addPartPrice = textInputAddPartPrice.getEditText();
        addPartDescription = textInputAddPartDescription.getEditText();
        addPartNegotiable = findViewById(R.id.addPartNegotiable);
        TwoDecimalPlacesInputFilter filter = new TwoDecimalPlacesInputFilter();
        addPartPrice.setFilters(new InputFilter[]{filter});

        uriImages = new ArrayList<>();

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

        addpartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        addPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String makeChoice = make.getSelectedItem().toString();
                String modelChoice = model.getSelectedItem().toString();
                String yearChoice = year.getSelectedItem().toString();
                String categoryChoice = category.getSelectedItem().toString();
                String subcategoryChoice = subcategories.getSelectedItem().toString();
                String conditionChoice = condition.getSelectedItem().toString();
                boolean nego = addPartNegotiable.isChecked();
                String name = addPartName.getText().toString();
                String price = addPartPrice.getText().toString();
                String description = addPartDescription.getText().toString();

                if (makeChoice.equals("Select Make") || modelChoice.equals("Select Model") || yearChoice.equals("Select Year") || categoryChoice.equals("Select Category") || subcategoryChoice.equals("Select SubCategory") || conditionChoice.equals("Select Condition") || name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddPartsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!uriImages.isEmpty()) {
                        uploadImages(uriImages);
                        PartController.addPart(AddPartsActivity.this, name, makeChoice, modelChoice, yearChoice, categoryChoice, subcategoryChoice, description, conditionChoice, price, nego, imagePaths, new PartController.PartCallback() {
                            @Override
                            public void onResponse(String status, String message) {
                                if (status.equals("success")) {
                                    Toast.makeText(AddPartsActivity.this, message, Toast.LENGTH_SHORT).show();
                                    restartApp();
                                } else {
                                    Toast.makeText(AddPartsActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(AddPartsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(AddPartsActivity.this, "Please select images", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            private void restartApp() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        imageList = new ArrayList<>();
        adapter = new ImageAddAdapter(this, imageList, this);
        addPartHorizontalScrollView.setAdapter(adapter);

    }

    public void addImage(String imageRes) {
        imageList.add(imageRes);
        adapter.notifyDataSetChanged();
        if (!imageList.isEmpty()) {
            addPartCardView.setVisibility(View.GONE);
        }

    }


    public void deleteImage() {
        int p = addPartHorizontalScrollView.getCurrentItem();
        if (adapter != null && p >= 0 && p < imageList.size()) {
            uriImages.remove(p);
            imageList.remove(p);
            addPartHorizontalScrollView.setAdapter(new ImageAddAdapter(this, imageList, this));
            addPartHorizontalScrollView.setCurrentItem(p - 1, true);
        }

        if (imageList.isEmpty()) {
            addPartCardView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onImageRemoved(int position) {
        deleteImage();
    }


    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View DialogView = LayoutInflater.from(AddPartsActivity.this).inflate(R.layout.picture_dialog, null);
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
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:

                    if (data != null && data.getData() != null) {
                        addImage(data.getData().toString());
                        uriImages.add(data.getData());
                    } else if (data != null && data.getClipData() != null) {
                        // Handle multiple image selection
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            addImage(uri.toString());
                            uriImages.add(uri);
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
                                uriImages.add(Uri.fromFile(imageFile));
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
            }
        }
    }

    private void uploadImages(ArrayList<Uri> imagesuri) {
        for (int i = 0; i < imagesuri.size(); i++) {
            String filePath = null;

            if (Objects.equals(imagesuri.get(i).getScheme(), "content")) {
                filePath = getRealPath(this, imagesuri.get(i));
            } else if (Objects.equals(imagesuri.get(i).getScheme(), "file")) {
                filePath = imagesuri.get(i).getPath();
            }
            ContentResolver cr = this.getContentResolver();
            typeOfeachImage = cr.getType(imagesuri.get(i));
            if (typeOfeachImage != null) {
                nameOfeachImage = "icon_" + System.currentTimeMillis() + "." + typeOfeachImage.replace("image/", "");
                imagePaths.add(nameOfeachImage);
            } else {
                nameOfeachImage = "icon_" + System.currentTimeMillis() + ".jpg";
                imagePaths.add(nameOfeachImage);
            }
            ImageUploaderClass.uploadImage(filePath, nameOfeachImage, "images/parts", new ImageUploaderClass.onSuccessfulTask() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String error) {
                }
            });
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