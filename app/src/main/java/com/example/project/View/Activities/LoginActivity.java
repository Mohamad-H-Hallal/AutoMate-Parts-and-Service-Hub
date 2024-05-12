package com.example.project.View.Activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.project.Controller.UserController;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;


public class LoginActivity extends BaseActivity implements UserController.AuthenticationCallback {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String language = getLanguagePreference();
        if (!language.isEmpty()) {
            setLocale(language);
        }
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textInputEmail = findViewById(R.id.textInputEmail);
        editTextEmail = textInputEmail.getEditText();
        textInputPassword = findViewById(R.id.textInputPassword);
        editTextPassword = textInputPassword.getEditText();

    }

    public void login(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (!(email.isEmpty() || password.isEmpty())) {
            UserController.authenticateUser(this, email, password, this);
        } else {
            Toast.makeText(this, "Please enter your credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(String response) {
        startActivity(new Intent(this, BottomNavMenuActivity.class));
        finish();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    public void onRegisterClick(View View) {
        editTextEmail.setText("");
        editTextPassword.setText("");
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void resetPassword(View view) {
        Intent resetPass = new Intent(this, EditPartActivity.class);
        startActivity(resetPass);
        finish();
    }

    private String getLanguagePreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("language", "");
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }

}