package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PartController {

    public ArrayList<String> imagespath(Context context, int part_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final ArrayList<String> imageUrls = new ArrayList<>();
        String url = IP + "/get_part_images.php?partId=" + part_id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse the JSON response to get image URLs
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String imageUrl = response.getString(i);
                                imageUrls.add(imageUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        requestQueue.add(jsonArrayRequest);

        return imageUrls;
    }

}
