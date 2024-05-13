package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Model.MechanicModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.Model.UserModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    public static void authenticateUser(final Context context, final String email, final String password, final AuthenticationCallback callback) {
        String url = IP + "/authenticate.php";

        RequestQueue queue = Volley.newRequestQueue(context);

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            if (status.equals("success")) {
                                int id = jsonResponse.getInt("id");
                                String accountType = jsonResponse.getString("account_type");
                                UserData userData = new UserData(context);
                                userData.setUserData(id, accountType);
                                callback.onSuccess("success");
                            } else {
                                String message = jsonResponse.getString("message");
                                callback.onError(message);
                            }
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
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public interface AuthenticationCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void registerUser(final Context context, final String name, final String email, final String password, final double latitude, final double longitude, final String accountType, final String icon, final String phone, final String specialization, final RegistrationCallback callback) {
        String url = Configuration.IP + "/register.php";

        try {
            String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") +
                    "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") +
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") +
                    "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(latitude), "UTF-8") +
                    "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(longitude), "UTF-8") +
                    "&" + URLEncoder.encode("account_type", "UTF-8") + "=" + URLEncoder.encode(accountType, "UTF-8") +
                    "&" + URLEncoder.encode("icon", "UTF-8") + "=" + URLEncoder.encode(icon, "UTF-8") +
                    "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") +
                    "&" + URLEncoder.encode("specialization", "UTF-8") + "=" + URLEncoder.encode(specialization, "UTF-8");

            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    callback.onRegistrationSuccess(response);
                }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onRegistrationError("Error: " + error.getMessage());
                }
            }) {
                @Override
                public byte[] getBody() {
                    try {
                        return data.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

            queue.add(stringRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.onRegistrationError("Error: " + e.getMessage());
        }
    }

    public interface RegistrationCallback {
        void onRegistrationSuccess(String response);
        void onRegistrationError(String error);
    }

    public interface UserDataListener {
        void onUserDataReceived(UserModel user);
        void onError(VolleyError error);
    }

    public void getUserData(Context context, int user_id, UserDataListener listener) {

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, IP + "select_user.php?id="+user_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String name = response.getString("name");
                            String email = response.getString("email");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            String icon = response.getString("icon");

                            // Assuming userData.getAccountType() is a synchronous method
                            UserData userData = new UserData(context);
                            UserModel user = new UserModel(user_id, name, email, latitude, longitude, userData.getAccountType(), icon);

                            // Pass the UserModel object to the listener
                            listener.onUserDataReceived(user);
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            e.printStackTrace();
                            // Pass the error to the listener
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


    public interface MechanicDataListener {
        void onMechanicDataReceived(MechanicModel user);
        void onError(VolleyError error);
    }

    public void getMechanicData(Context context, int user_id, MechanicDataListener listener) {
        // Create a StringRequest with POST method
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, IP + "select_user.php?id="+user_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String name = response.getString("name");
                            String email = response.getString("email");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            String icon = response.getString("icon");
                            String date = response.getString("date");
                            String end_date = response.getString("end_date");
                            String phone = response.getString("phone");
                            String specialization = response.getString("specialization");
                            String biography = response.getString("biography");
                            String subscription = response.getString("subscription");
                            int year_of_experience = response.getInt("year_of_experience");
                            float rating = (float) response.getDouble("rating");

                            // Assuming userData.getAccountType() is a synchronous method
                            UserData userData = new UserData(context);
                            MechanicModel user = new MechanicModel(name, email, latitude, longitude, userData.getAccountType(), icon, date,end_date, phone, specialization, biography, subscription, user_id, year_of_experience, rating);

                            // Pass the UserModel object to the listener
                            listener.onMechanicDataReceived(user);
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            e.printStackTrace();
                            // Pass the error to the listener
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

    public interface ScrapyardDataListener {
        void onScrapyardDataReceived(ScrapyardModel user);
        void onError(VolleyError error);
    }

    public void getScrapyardData(Context context, int user_id, ScrapyardDataListener listener) {

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, IP + "select_user.php?id="+user_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String name = response.getString("name");
                            String email = response.getString("email");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            String icon = response.getString("icon");
                            String date = response.getString("date");
                            String end_date = response.getString("end_date");
                            String phone = response.getString("phone");
                            String specialization = response.getString("specialization");
                            String biography = response.getString("biography");
                            String subscription = response.getString("subscription");
                            float rating = (float) response.getDouble("rating");


                            UserData userData = new UserData(context);
                            ScrapyardModel user = new ScrapyardModel(name, email, userData.getAccountType(), icon, date,end_date, phone, specialization, biography, subscription, user_id, latitude, longitude, rating);


                            listener.onScrapyardDataReceived(user);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void updateUserImage(Context context,int user_id,String name_of_image){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, IP+"update_user_image.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(user_id)); // Replace with actual user ID
                params.put("icon", name_of_image);
                return params;
            }
        };
        queue.add(request);
    }


    public void updateUser(Context context, int userId, String name, double longitude, double latitude) {
        updateUser(context, userId, name, longitude, latitude, 0, "", "", "");
    }

    public void updateMechanic(Context context, int userId, String name, double longitude, double latitude, int yearOfExperience, String specialization, String biography, String phone) {
        updateUser(context, userId, name, longitude, latitude, yearOfExperience, specialization, biography, phone);
    }

    public void updateScrapyard(Context context, int userId, String name, double longitude, double latitude, String specialization, String biography, String phone) {
        updateUser(context, userId, name, longitude, latitude, 0, specialization, biography, phone);
    }

    private void updateUser(Context context, int userId, String name, double longitude, double latitude, int yearOfExperience, String specialization, String biography, String phone) {
      try{
        RequestQueue queue = Volley.newRequestQueue(context);
        String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userId), "UTF-8") +
                "&"+URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+
                "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(longitude), "UTF-8")+
                "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(latitude), "UTF-8")+
                "&" + URLEncoder.encode("year_of_experience", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(yearOfExperience), "UTF-8")+
                "&" + URLEncoder.encode("specialization", "UTF-8") + "=" + URLEncoder.encode(specialization, "UTF-8")+
                "&" + URLEncoder.encode("biography", "UTF-8") + "=" + URLEncoder.encode(biography, "UTF-8")+
                "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

        StringRequest request = new StringRequest(Request.Method.POST, IP + "update_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("test",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test",error.toString());
                    }
                }){
            @Override
            public byte[] getBody() {
                try {
                    return data.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        queue.add(request);}
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        Log.d("Error: " , e.getMessage());
    }

    }




}
