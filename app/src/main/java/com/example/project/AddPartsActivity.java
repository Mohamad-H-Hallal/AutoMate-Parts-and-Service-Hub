package com.example.project;

import static com.example.project.GetImagePath.getRealPath;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.project.FileUpload.ImageUploaderClass;

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

public class AddPartsActivity extends BaseActivity {
private static final int CAMERA_REQUEST_CODE=150;
private static final int GALLERY_REQUEST_CODE=170;
    private ImageButton back;
    private CardView addPartCardView;
    List<String> imageList;
    ImageEditAdapter adapter;
    private ViewPager  addPartHorizontalScrollView;
    TextView addpartimage;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String typeOfeachImage = "";
    private String nameOfeachImage = "";
    ArrayList<Uri> uriImages;
    private AppCompatButton addPartButton;
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
                if(!uriImages.isEmpty()){
                uploadImages(uriImages);}
            }
        });

        imageList = new ArrayList<>();
        for (int i = 0; i <uriImages.size();i++){
            imageList.add(uriImages.get(i).toString());
        }
        adapter =new ImageEditAdapter(getSupportFragmentManager(),imageList);
        addPartHorizontalScrollView.setAdapter(adapter);

    }

    public void addImage(String imageRes) {
        if(!imageList.isEmpty()) {
            addPartCardView.setVisibility(View.GONE);
        }
            imageList.add(imageRes);
            adapter.notifyDataSetChanged();
    }

    public void deleteImage(String imageRes) {
        int position = imageList.indexOf(imageRes);
        if (position != -1) {
            imageList.remove(position);
            adapter.notifyDataSetChanged();
        }
        if(imageList.isEmpty()) {
            addPartCardView.setVisibility(View.VISIBLE);
        }
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

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent chooser = Intent.createChooser(cameraIntent, "Select Image Source");
            startActivityForResult(chooser, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

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
}