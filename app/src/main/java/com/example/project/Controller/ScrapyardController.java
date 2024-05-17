package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.project.Model.ScrapyardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScrapyardController {


    public interface AllScrapyardDataListener {
        void onAllScrapyardDataReceived(List<ScrapyardModel> all_scrapyard);

        void onError(VolleyError error);
    }

    public void getAllScrapyardsData(Context context, int user_id, String filtered, ScrapyardController.AllScrapyardDataListener listener) {

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, IP + "get_all_scrapyards.php?filtered=" + filtered + "&user_id=" + user_id,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ScrapyardModel> all_scrapyards = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                double latitude = jsonObject.getDouble("latitude");
                                double longitude = jsonObject.getDouble("longitude");
                                String icon = jsonObject.getString("icon");
                                String date = jsonObject.getString("date");
                                String end_date = jsonObject.getString("end_date");
                                String phone = jsonObject.getString("phone");
                                String specialization = jsonObject.getString("specialization");
                                String biography = jsonObject.optString("biography", null);
                                String subscription = jsonObject.getString("subscription");
                                float rating = (float) jsonObject.getDouble("rating");
                                String user_type = jsonObject.getString("account_type");

                                ScrapyardModel scrapyard = new ScrapyardModel(name, email, user_type, icon, date, end_date, phone, specialization, biography, subscription, id, latitude, longitude, rating);
                                all_scrapyards.add(scrapyard);
                            }

                            listener.onAllScrapyardDataReceived(all_scrapyards);
                        } catch (JSONException e) {

                            e.printStackTrace();

                            listener.onError(new VolleyError("JSON parsing error: " + e.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Volley error
                        error.printStackTrace();

                        listener.onError(error);
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface ScrapyardRateListener {
        void onrateScrapyardDataReceived(Float rate);

        void onError(VolleyError error);
    }

    public void submitRating(Context context, float rate, int user_id, int scrapyard_id, ScrapyardController.ScrapyardRateListener listener) {
        String url = IP + "submit_rating_scrapyard.php?rate=" + rate + "&user_id=" + user_id + "&scrapyard_id=" + scrapyard_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("test", response);
                            float newRating = Float.parseFloat(response);
                            listener.onrateScrapyardDataReceived(newRating);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            listener.onError(new VolleyError("Rating response parsing error: " + e.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle Volley error
                        error.printStackTrace();
                        listener.onError(error);
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
