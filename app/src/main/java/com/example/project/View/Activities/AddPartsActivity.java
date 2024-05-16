package com.example.project.View.Activities;

import static com.example.project.Controller.GetImagePath.getRealPath;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.example.project.Controller.PartController;
import com.example.project.Controller.TwoDecimalPlacesInputFilter;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.R;
import com.example.project.View.Adapters.ImageAddAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private Spinner make, model, year, category, subcategory, condition;
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
        subcategory = findViewById(R.id.addPartSubCategorySpinner);
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
        imagePaths = new ArrayList<>();
        uriImages = new ArrayList<>();

        Intent intent = getIntent();
        String sendCategory = intent.getStringExtra("category");
        String sendSubcategory = intent.getStringExtra("subcategory");

        fillSpinners(sendCategory, sendSubcategory);

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
                Intent i = new Intent(AddPartsActivity.this, ManagePartsActivity.class);
                i.putExtra("category",sendCategory);
                i.putExtra("subcategory",sendSubcategory);
                startActivity(i);
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
//                Context englishContext = new EnglishContextWrapper(AddPartsActivity.this);
                String makeChoice = getResources().getStringArray(R.array.make_choices)[make.getSelectedItemPosition()];
                String yearChoice = getResources().getStringArray(R.array.year_choices_combined)[year.getSelectedItemPosition()];
                String categoryChoice = getResources().getStringArray(R.array.categories_choices_combined)[category.getSelectedItemPosition()];
                String conditionChoice = getResources().getStringArray(R.array.condition_choices_combined)[condition.getSelectedItemPosition()];

                String subcategoriesArray = getResources().getStringArray(R.array.subcategories_choices_combined)[category.getSelectedItemPosition()];
                String[] subcategoryChoices = subcategoriesArray.split(";");
                String subcategoryChoice = subcategoryChoices[subcategory.getSelectedItemPosition()];

                String modelArray = getResources().getStringArray(R.array.model_choices)[make.getSelectedItemPosition()];
                String[] modelChoices = modelArray.split(";");
                String modelChoice = modelChoices[model.getSelectedItemPosition()];

                boolean nego = addPartNegotiable.isChecked();
                String name = addPartName.getText().toString();
                String price = addPartPrice.getText().toString();
                String description = addPartDescription.getText().toString();

                if (makeChoice.equals(make.getItemAtPosition(0).toString()) || modelChoice.equals(model.getItemAtPosition(0).toString()) || yearChoice.equals(year.getItemAtPosition(0).toString()) || categoryChoice.equals(category.getItemAtPosition(0).toString()) || subcategoryChoice.equals(subcategory.getItemAtPosition(0).toString()) || conditionChoice.equals(condition.getItemAtPosition(0).toString()) || name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddPartsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!uriImages.isEmpty()) {
                        uploadImages(uriImages);
                        PartController.addPart(AddPartsActivity.this, name, makeChoice, modelChoice, yearChoice, categoryChoice, subcategoryChoice, description, conditionChoice, Double.parseDouble(price), nego, imagePaths, new PartController.PartCallback() {
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
        });

        imageList = new ArrayList<>();
        adapter = new ImageAddAdapter(this, imageList, this);
        addPartHorizontalScrollView.setAdapter(adapter);

    }

//    // EnglishContextWrapper class definition
//    public static class EnglishContextWrapper extends ContextWrapper {
//        public EnglishContextWrapper(Context base) {
//            super(base);
//        }
//
//        @Override
//        public Resources getResources() {
//            Configuration configuration = new Configuration(super.getResources().getConfiguration());
//            configuration.setLocale(new Locale("en"));
//            return createConfigurationContext(configuration).getResources();
//        }
//    }

    public void fillSpinners(String sendCategory, String sendSubcategory) {

        Map<String, List<String>> categorySubcategoryMap = new HashMap<>();
        Map<String, List<String>> makeModelMap = new HashMap<>();
        String[] categoriesArray = getResources().getStringArray(R.array.categories_choices);
        String[] years = getResources().getStringArray(R.array.year_choices);
        String[] subcategoriesArray = getResources().getStringArray(R.array.subcategories_choices);
        String[] makeArray = getResources().getStringArray(R.array.make_choices);
        String[] modelArray = getResources().getStringArray(R.array.model_choices);
        String[] conditionArray = getResources().getStringArray(R.array.condition_choices);

        for (int i = 0; i < categoriesArray.length; i++) {
            String category = categoriesArray[i];
            String[] subcategories = subcategoriesArray[i].split(";");
            categorySubcategoryMap.put(category, Arrays.asList(subcategories));
        }

        for (int i = 0; i < makeArray.length; i++) {
            String make = makeArray[i];
            String[] model = modelArray[i].split(";");
            makeModelMap.put(make, Arrays.asList(model));
        }

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditionArray);
        condition.setAdapter(conditionAdapter);
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeArray);
        make.setAdapter(makeAdapter);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        year.setAdapter(yearAdapter);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesArray);
        category.setAdapter(categoryAdapter);

        make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMake = (String) parent.getItemAtPosition(position);
                List<String> modelList = makeModelMap.get(selectedMake);
                ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(AddPartsActivity.this, android.R.layout.simple_spinner_item, modelList);
                model.setAdapter(modelAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        String[] categorySplit = sendCategory.split("-");
        String[] subcategorySplit = sendSubcategory.split("-");
        String sentCategory = "";
        String sentSubcategory = "";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddPartsActivity.this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            sentCategory = categorySplit[0];
            sentSubcategory = subcategorySplit[0];
        } else if (selectedLanguage.equals("ar")) {
            sentCategory = categorySplit[1];
            sentSubcategory = subcategorySplit[1];
        }

        if (sentCategory != null) {
            int categoryPosition = categoryAdapter.getPosition(sentCategory);
            category.setSelection(categoryPosition);
            String selectedCategory = (String) category.getItemAtPosition(categoryPosition);
            List<String> subcategoriesList = categorySubcategoryMap.get(selectedCategory);
            ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcategoriesList);
            subcategory.setAdapter(subcategoryAdapter);
            if (sentSubcategory != null) {
                int subcategoryPosition = subcategoryAdapter.getPosition(sentSubcategory);
                subcategory.setSelection(subcategoryPosition);
            }
        }

    }

    private void restartApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent chooser = Intent.createChooser(cameraIntent, "Select Image Source");
            startActivityForResult(chooser, CAMERA_REQUEST_CODE);
        }
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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