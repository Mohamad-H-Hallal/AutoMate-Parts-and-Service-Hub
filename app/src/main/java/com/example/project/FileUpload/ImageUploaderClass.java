package com.example.project.FileUpload;



import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploaderClass {

    public interface onSuccessfulTask {
        void onSuccess();
        void onFailed(String error);
    }

    public static void uploadImage(String filePath,String name, String folder_name,  onSuccessfulTask task) {
        try {
            AppImagesService apiInterface = RetrofitApiClient.getClient().create(AppImagesService.class);
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", name, requestFile);
            MultipartBody.Part folder = MultipartBody.Part.createFormData("folder", folder_name);
            Call<String> call = apiInterface.UploadImage(body, folder);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    String responseBody = response.body();

                    if (responseBody != null) {
                        if ("ok".equals(responseBody)) {
                            task.onSuccess();
                        } else {
                            task.onFailed(responseBody);
                        }
                    } else {

                        task.onFailed(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    task.onFailed(t.getMessage());
                }

            });
        } catch (Exception e) {
            Log.e("ImageUploader", "Error uploading image", e);
            task.onFailed(e.getMessage());
        }
    }

}