package com.example.project.FileUpload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AppFilesService {
    @Multipart
    @POST("upload_files.php")
    Call<String> UploadFile(
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part folder,
            @Part MultipartBody.Part user_id
    );
}
