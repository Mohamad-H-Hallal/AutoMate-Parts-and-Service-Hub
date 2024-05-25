package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EnterOtpActivity extends BaseActivity {

    private TextInputLayout textInputOTP;
    private EditText editTextOTP;
    private AppCompatButton sendOTPButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputOTP = findViewById(R.id.textInputOTPForPassReset);
        editTextOTP = textInputOTP.getEditText();
        sendOTPButton = findViewById(R.id.sendOTPButton);
        email = getIntent().getStringExtra("email");

        sendOTPButton.setOnClickListener(v -> {
            String otp = editTextOTP.getText().toString().trim();
            if (!otp.isEmpty()) {
                verifyOtp(email, otp);
            } else {
                Toast.makeText(this, "Please enter the OTP.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp(String email, String otp) {
        String url = IP + "/verify_otp.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (status.equals("success")) {
                            Intent intent = new Intent(EnterOtpActivity.this, ResetActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("otp", otp);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}