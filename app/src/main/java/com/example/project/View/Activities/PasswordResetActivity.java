package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordResetActivity extends BaseActivity {

    private TextInputLayout textInputEmail;
    private EditText editTextEmail;
    private AppCompatButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputEmail = findViewById(R.id.textInputEmailForPassReset);
        editTextEmail = textInputEmail.getEditText();
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                requestOtp(email);
            } else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestOtp(String email) {
        String url = IP + "/request_otp.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("PasswordResetActivity", "Response: " + response); // Log the response
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("status") && jsonResponse.has("message")) {
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                            if (status.equals("success") || status.equals("otp_exists")) {
                                Intent intent = new Intent(this, EnterOtpActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(this, "Invalid server response.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
