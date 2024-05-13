package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Model.PartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void fetchParts(Context context,final PartFetchListener listener) {

        RequestQueue mRequestQueue=Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, IP+"get_all_parts.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<PartModel> parts = new ArrayList<>();
                            ArrayList<String> image_path = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                PartModel part = new PartModel();
                                part.setId(jsonObject.getInt("id"));
                                part.setName(jsonObject.getString("name"));
                                part.setMake(jsonObject.getString("make"));
                                part.setModel(jsonObject.getString("model"));
                                part.setYear(jsonObject.getInt("year"));
                                part.setScrapyard_id(jsonObject.getInt("scrapyard_id"));
                                part.setPart_condition(jsonObject.getString("part_condition"));
                                part.setCategory(jsonObject.getString("category"));
                                part.setSubcategory(jsonObject.getString("subcategory"));
                                part.setDescription(jsonObject.getString("description"));
                                part.setNegotiable(Boolean.parseBoolean(jsonObject.getString("negotiable")));
                                part.setPrice(jsonObject.getDouble("price"));
                                parts.add(part);
                                if (!jsonObject.isNull("image_path")) {
                                    image_path.add(jsonObject.getString("image_path"));
                                } else {

                                    image_path.add("default_image_path.png");
                                }
                            }

                            listener.onPartsFetched(parts,image_path);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call listener with error
                        listener.onError(error.toString());
                    }
                });

        mRequestQueue.add(jsonArrayRequest);
    }

    public interface PartFetchListener {
        void onPartsFetched(List<PartModel> parts,ArrayList<String> image_path);
        void onError(String error);
    }

}
