package com.example.project.View.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class PasswordResetActivity extends BaseActivity {

    private TextInputLayout textInputEmail;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputEmail = findViewById(R.id.textInputEmailForPassReset);
        editTextEmail = textInputEmail.getEditText();
    }

//    public void resetPassword(View view) {
//        String email = editTextEmail.toString();
//        String url = "http://192.168.1.106:80/Project/reset_password.php";
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Handle the response from the server
//                        if (response.equals("success")) {
//                            Toast.makeText(PasswordResetActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(PasswordResetActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle errors
//                Toast.makeText(PasswordResetActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // Parameters to send to the backend script
//                Map<String, String> params = new HashMap<>();
//                params.put("email", email);
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);
//    }
}