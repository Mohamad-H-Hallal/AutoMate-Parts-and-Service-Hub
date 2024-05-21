package com.example.project.FileUpload;

import com.example.project.Model.DiagnosticDataModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AppFilesService {
    @Multipart
    @POST("upload_files.php")
    Call<String> UploadFile(
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part folder,
            @Part MultipartBody.Part user_id
    );

    @GET("get_recent_files.php")
    Call<List<DiagnosticDataModel>> getRecentFiles();

    @GET("filter_files.php")
    Call<List<DiagnosticDataModel>> filterFiles(
            @Query("day") String day,
            @Query("month") String month,
            @Query("year") String year,
            @Query("hour") String hour
    );
}