package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.Model.MechanicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MechanicController {

    public interface AllMechanicDataListener {
        void onAllMechanicDataReceived(List<MechanicModel> all_mechanics);

        void onError(VolleyError error);
    }
    public void getAllMechanicsData(Context context,String user_id,String filtered, MechanicController.AllMechanicDataListener listener) {

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, IP + "get_all_mechanics.php? filtered ="+filtered+" & user_id = "+user_id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<MechanicModel> all_mechanics = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("mechanic_id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                double latitude = jsonObject.getDouble("latitude");
                                double longitude = jsonObject.getDouble("longitude");
                                String icon = jsonObject.getString("icon");
                                String date = jsonObject.getString("date");
                                String end_date = jsonObject.getString("end_date");
                                String phone = jsonObject.getString("phone");
                                String specialization = jsonObject.getString("specialization");
                                String biography = jsonObject.getString("biography");
                                String subscription = jsonObject.getString("subscription");
                                int year_of_experience = jsonObject.getInt("year_of_experience");
                                float rating = (float) jsonObject.getDouble("rating");
                                String user_type = jsonObject.getString("account_type");

                                MechanicModel mechanic = new MechanicModel(name, email, latitude, longitude, user_type, icon, date, end_date, phone, specialization, biography, subscription, id, year_of_experience, rating);
                            all_mechanics.add(mechanic);
                            }

                            listener.onAllMechanicDataReceived(all_mechanics);
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
                        // Pass the error to the listener
                        listener.onError(error);
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
