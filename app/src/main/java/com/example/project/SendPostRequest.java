package com.example.project;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.example.project.View.Fragments.DiagnosticsFragment;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendPostRequest extends AsyncTask<String, Void, String> {

    private Context context;

    public SendPostRequest(Context context) {
        this.context = context;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected String doInBackground(String... params) {
        String serverIpAddress = params[0];
        String macAddress = params[1];

        OkHttpClient client = new OkHttpClient();
        String json = "{ \"target_device_address\": \"" + macAddress + "\" }";
        RequestBody body = RequestBody.create(JSON, json);
        String url = "http://" + serverIpAddress + ":5000/zip_and_send";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Response not successful: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.startsWith("Response not successful")) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } else if (result.startsWith("Error")) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data sent successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
