package com.example.project;

import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputEmail = findViewById(R.id.textInputEmail);
        editTextEmail = textInputEmail.getEditText();
        textInputPassword = findViewById(R.id.textInputPassword);
        editTextPassword = textInputPassword.getEditText();
    }

    public void login(View view) {
        startActivity(new Intent(this, BottomNavMenuActivity.class));
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void resetPassword(View view) {
        Intent resetPass = new Intent(this,password_rest.class);
        startActivity(resetPass);
    }

}