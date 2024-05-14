package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Model.PartModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void addPart(Context context, String name, String make, String model, int year, String category, String subcategory, String description, String condition, double price, boolean negotiable, ArrayList<String> images, final PartCallback callback) {

        String url = IP + "/add_part.php";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            callback.onResponse(status, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("make", make);
                params.put("model", model);
                params.put("year", String.valueOf(year));
                params.put("category", category);
                params.put("subcategory", subcategory);
                params.put("condition", condition);
                params.put("description", description);
                params.put("price", String.valueOf(price));
                params.put("id", String.valueOf(UserData.getId()));
                params.put("negotiable", String.valueOf(negotiable));
                for (int i = 0; i < images.size(); i++) {
                    params.put("images[" + i + "]", images.get(i));
                }
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public interface PartCallback {
        void onResponse(String status, String message);
        void onError(String error);
    }

}
