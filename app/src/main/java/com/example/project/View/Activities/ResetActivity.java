package com.example.project.View.Activities;

import static com.example.project.Controller.Configuration.IP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

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

public class ResetActivity extends AppCompatActivity {

    private TextInputLayout textInputPassReset, textInputConPassReset;
    private EditText editTextPass, editTextConPass;
    private AppCompatButton sendPassButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputPassReset = findViewById(R.id.textInputPassReset);
        editTextPass = textInputPassReset.getEditText();
        textInputConPassReset = findViewById(R.id.textInputConPassReset);
        editTextConPass = textInputConPassReset.getEditText();
        sendPassButton = findViewById(R.id.sendPassButton);
        email = getIntent().getStringExtra("email");

        sendPassButton.setOnClickListener(v -> {
            String newPassword = editTextPass.getText().toString().trim();
            String confirmPassword = editTextConPass.getText().toString().trim();
            if (!newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
                resetPassword(email, newPassword);
            } else {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword(String email, String newPassword) {
        String url = IP + "/reset_password.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (status.equals("success")) {
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
