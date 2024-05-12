package com.example.project.Controller;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserController {

    public static void authenticateUser(final Context context, final String email, final String password, final AuthenticationCallback callback) {
        String url = Configuration.IP + "/authenticate.php";

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
                                String id = jsonResponse.getString("id");
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
}
