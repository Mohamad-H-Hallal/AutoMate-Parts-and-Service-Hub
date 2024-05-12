package com.example.project.Controller;

import static com.example.project.Controller.Configuration.IP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Model.MechanicModel;
import com.example.project.Model.ScrapyardModel;
import com.example.project.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserController {

    public static void authenticateUser(final Context context, final String email, final String password, final AuthenticationCallback callback) {
        String url = IP + "/authenticate.php";

        try {
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") +
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(context);

            // Request a string response from the provided URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                int id = jsonResponse.getInt("id");
                                String accountType = jsonResponse.getString("account_type");
                                UserData userData = new UserData(context);
                                userData.setUserData(id, accountType);
                                callback.onSuccess("success");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.onError("Error: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error response
                    callback.onError("Error: " + error.getMessage());
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

            // Add the request to the RequestQueue
            queue.add(stringRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.onError("Error: " + e.getMessage());
        }
    }

    public interface AuthenticationCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public interface UserDataListener {
        void onUserDataReceived(UserModel user);
        void onError(VolleyError error);
    }

    public void getUserData(Context context, int user_id, UserDataListener listener) {
        // Create a StringRequest with POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "select_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");
                            String icon = jsonObject.getString("icon");

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
                }) {
            // Override the getParams method to include parameters for POST request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Add POST parameters, if any
                params.put("id", String.valueOf(user_id));
                return params;
            }
        };

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "select_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");
                            String icon = jsonObject.getString("icon");
                            String date = jsonObject.getString("date");
                            String phone = jsonObject.getString("phone");
                            String specialization = jsonObject.getString("specialization");
                            String biography = jsonObject.getString("biography");
                            String subscription = jsonObject.getString("subscription");
                            int year_of_experience = jsonObject.getInt("year_of_experience");
                            float rating = (float) jsonObject.getDouble("rating");

                            // Assuming userData.getAccountType() is a synchronous method
                            UserData userData = new UserData(context);
                            MechanicModel user = new MechanicModel(name, email, latitude, longitude, userData.getAccountType(), icon, date, phone, specialization, biography, subscription, user_id, year_of_experience, rating);

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
                }) {
            // Override the getParams method to include parameters for POST request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Add POST parameters, if any
                params.put("id", String.valueOf(user_id));
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface ScrapyardDataListener {
        void onScrapyardDataReceived(ScrapyardModel user);
        void onError(VolleyError error);
    }

    public void getScrapyardData(Context context, int user_id, ScrapyardDataListener listener) {
        // Create a StringRequest with POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "select_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");
                            String icon = jsonObject.getString("icon");
                            String date = jsonObject.getString("date");
                            String phone = jsonObject.getString("phone");
                            String specialization = jsonObject.getString("specialization");
                            String biography = jsonObject.getString("biography");
                            String subscription = jsonObject.getString("subscription");
                            float rating = (float) jsonObject.getDouble("rating");


                            UserData userData = new UserData(context);
                            ScrapyardModel user = new ScrapyardModel(name, email, userData.getAccountType(), icon, date, phone, specialization, biography, subscription, user_id, latitude, longitude, rating);


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
                }) {
            // Override the getParams method to include parameters for POST request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Add POST parameters, if any
                params.put("id", String.valueOf(user_id));
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
