package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;
import static com.example.project.Controller.GetImagePath.getRealPath;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Controller.PartController;
import com.example.project.Controller.ScrapyardController;
import com.example.project.Controller.TwoDecimalPlacesInputFilter;
import com.example.project.Controller.UserController;
import com.example.project.Controller.UserData;
import com.example.project.CustomMapView;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.Model.PartModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.R;
import com.example.project.View.Adapters.ImageEditAdapter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    TextView e_scrapyardname_detailtxt, add;
    private EditText e_name_part, e_pricetxt, e_descriptiontxt;
    private Spinner e_model_detailtxt, e_make_detailtxt, e_year_detailtxt, e_category_detailtxt, e_subcategory_detailtxt, e_condition_detailtxt;
    private CheckBox e_negotiable_detail;
    private AlertDialog Dialog;
    private String typeOfeachImage = "";
    private String nameOfeachImage = "";
    private ArrayList<Uri> uriImage;
    private double latitude = 33;
    private double longitude = 35;
    private static final int GALLERY_REQUEST_CODE = 110;
    private static final int CAMERA_REQUEST_CODE = 120;
    private static final int MAP_REQUEST_CODE = 130;
    PartController part_controller;
    UserController scrap_controller;
    String nameofscrap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_part);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        horizontalScrollView = findViewById(R.id.e_horizontalScrollView);
        miniMapView = findViewById(R.id.e_miniMapView);
        back = findViewById(R.id.back_arrow8);
        location = findViewById(R.id.e_location_part_click);
        phone = findViewById(R.id.e_phone_detailtxt);
        add = findViewById(R.id.e_add_image);
        e_name_part = findViewById(R.id.e_name_part);
        e_pricetxt = findViewById(R.id.e_pricetxt);
        TwoDecimalPlacesInputFilter filter = new TwoDecimalPlacesInputFilter();
        e_pricetxt.setFilters(new InputFilter[]{filter});
        e_descriptiontxt = findViewById(R.id.e_descriptiontxt);
        e_make_detailtxt = findViewById(R.id.e_make_detailtxt);
        e_model_detailtxt = findViewById(R.id.e_model_detailtxt);
        e_category_detailtxt = findViewById(R.id.e_category_detailtxt);
        e_subcategory_detailtxt = findViewById(R.id.e_subcategory_detailtxt);
        e_scrapyardname_detailtxt = findViewById(R.id.e_scrapyardname_detailtxt);
        e_year_detailtxt = findViewById(R.id.e_year_detailtxt);
        e_condition_detailtxt = findViewById(R.id.e_condition_detailtxt);
        e_negotiable_detail = findViewById(R.id.e_negotiable_detail);

        editpartdefault = findViewById(R.id.editpartdefault);
        part_controller = new PartController();
        scrap_controller = new UserController();
        uriImage = new ArrayList<Uri>();
        imageListfromdb = new ArrayList<>();
        imageListfromuser = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        Intent i = getIntent();
        String part_id = i.getStringExtra("part_id");
        filltheimages(part_id);

        scrap_controller.getScrapyardData(this, UserData.getId(), new UserController.ScrapyardDataListener() {
            @Override
            public void onScrapyardDataReceived(ScrapyardModel user) {
                longitude = user.getLongitude();
                latitude = user.getLatitude();
                e_scrapyardname_detailtxt.setText(user.getName());
                phone.setText(user.getPhone());
            }

            @Override
            public void onError(VolleyError error) {

            }
        });


        part_controller.fetchthePart(this, Integer.parseInt(part_id), new PartController.ParttheFetchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onthePartFetched(PartModel parts) {
                e_name_part.setText(parts.getName());
                e_pricetxt.setText("" + parts.getPrice());
                e_descriptiontxt.setText(parts.getDescription());
                e_negotiable_detail.setChecked(parts.isNegotiable());

                String[] cat = parts.getCategory().split("-");
                String[] subcat = parts.getSubcategory().split("-");
                String mod = parts.getModel();
                String mak = parts.getMake();
                String[] yea = parts.getYear().split("-");
                String[] cond = parts.getPart_condition().split("-");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditPartActivity.this);
                String selectedLanguage = preferences.getString("selected_language", "");
                Log.d("test",selectedLanguage);
                if (selectedLanguage.equals("en")) {

                    fillSpinners(cat[0], subcat[0], mod, mak, yea[0], cond[0]);
                } else if (selectedLanguage.equals("ar")) {
                    fillSpinners(cat[1], subcat[1], mod, mak, yea[1], cond[1]);
                }

            }

            @Override
            public void onError(String error) {
                Log.e("error", error);
            }
        });


        miniMapView.onCreate(savedInstanceState);
        miniMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Customize the map as needed
                LatLng location = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(location).title(nameofscrap).snippet("ScrapYard"));
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
                Intent i = new Intent(getApplicationContext(), MapsLocationActivity.class);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

    }

    public void filltheimages(String part_id) {
        part_controller.getImagesPath(this, Integer.parseInt(part_id), new PartController.ImagePathListener() {
            @Override
            public void onImagePathReceived(ArrayList<String> imageUrls) {
                imageListfromdb = imageUrls;
                if (imageListfromuser.isEmpty() && imageListfromdb.isEmpty()) {
                    editpartdefault.setVisibility(View.VISIBLE);
                } else {
                    editpartdefault.setVisibility(View.GONE);
                    adapter = new ImageEditAdapter(EditPartActivity.this, imageListfromdb, imageListfromuser, EditPartActivity.this);
                    horizontalScrollView.setAdapter(adapter);
                }

            }
        });
    }

    public void fillSpinners(String sendCategory, String sendSubcategory, String model, String make, String year, String condition) {

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
            String makee = makeArray[i];
            String[] modell = modelArray[i].split(";");
            makeModelMap.put(makee, Arrays.asList(modell));
        }

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditionArray);
        e_condition_detailtxt.setAdapter(conditionAdapter);
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeArray);
        e_make_detailtxt.setAdapter(makeAdapter);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        e_year_detailtxt.setAdapter(yearAdapter);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesArray);
        e_category_detailtxt.setAdapter(categoryAdapter);

        if (year != null) {
            int yearPosition = yearAdapter.getPosition(year);
            e_year_detailtxt.setSelection(yearPosition);
        }
        if (condition != null) {
            int conditionPosition = conditionAdapter.getPosition(condition);
            e_condition_detailtxt.setSelection(conditionPosition);
        }

        if (make != null) {
            int makePosition = makeAdapter.getPosition(make);
            e_make_detailtxt.setSelection(makePosition);
            String selectedMake = (String) e_make_detailtxt.getItemAtPosition(makePosition);
            List<String> modelList = makeModelMap.get(selectedMake);
            ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelList);
            e_model_detailtxt.setAdapter(modelAdapter);
            if (model != null) {
                int modelPosition = modelAdapter.getPosition(model);
                e_model_detailtxt.setSelection(modelPosition);
            }

        }

        if (sendCategory != null) {
            int categoryPosition = categoryAdapter.getPosition(sendCategory);
            e_category_detailtxt.setSelection(categoryPosition);
            String selectedCategory = (String) e_category_detailtxt.getItemAtPosition(categoryPosition);
            List<String> subcategoriesList = categorySubcategoryMap.get(selectedCategory);
            ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subcategoriesList);
            e_subcategory_detailtxt.setAdapter(subcategoryAdapter);
            if (sendSubcategory != null) {
                int subcategoryPosition = subcategoryAdapter.getPosition(sendSubcategory);
                e_subcategory_detailtxt.setSelection(subcategoryPosition);
            }

        }
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View DialogView = LayoutInflater.from(EditPartActivity.this).inflate(R.layout.picture_dialog, null);
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


    public void addImage(String imageRes) {
        imageListfromuser.add(imageRes);
        adapter.notifyDataSetChanged();
        if (!imageListfromuser.isEmpty() || !imageListfromdb.isEmpty()) {
            editpartdefault.setVisibility(View.GONE);
        }
    }

    public void deleteImage(int position) {
        if (position < imageListfromdb.size()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "delete_image.php?path=" + imageListfromdb.get(position), response -> {
                Log.d("test", response);
                Toast.makeText(EditPartActivity.this, response.trim(), Toast.LENGTH_SHORT).show();

                imageListfromdb.remove(position);
                horizontalScrollView.setAdapter(new ImageEditAdapter(this, imageListfromdb, imageListfromuser, this));

                if (imageListfromuser.isEmpty() && imageListfromdb.isEmpty()) {
                    editpartdefault.setVisibility(View.VISIBLE);
                }
            }, error -> {

            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {

            int adjustedPosition = position - imageListfromdb.size();
            imageListfromuser.remove(adjustedPosition);
            uriImage.remove(adjustedPosition);
            horizontalScrollView.setAdapter(new ImageEditAdapter(this, imageListfromdb, imageListfromuser, this));

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