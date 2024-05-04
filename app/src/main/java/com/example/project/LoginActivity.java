package com.example.project;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;


public class LoginActivity extends BaseActivity {
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
        startActivity(new Intent(this, BottomNavMenuActivity.class));
        finish();
    }

    public void onRegisterClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void resetPassword(View view) {
        Intent resetPass = new Intent(this, PartDetailsActivity.class);
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