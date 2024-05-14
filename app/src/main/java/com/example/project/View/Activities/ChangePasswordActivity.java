package com.example.project.View.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.Controller.UserController;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageButton back;
    private AppCompatButton change;
    private EditText oldPass,newPass,confirmPass;
    private TextInputLayout ti1, ti2, ti3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_change_password);
        back = findViewById(R.id.back_arrow9);
        change = findViewById(R.id.change_pass);
        ti1 = findViewById(R.id.ti1);
        ti2 = findViewById(R.id.ti2);
        ti3 = findViewById(R.id.ti3);
        oldPass = ti1.getEditText();
        newPass = ti2.getEditText();
        confirmPass = ti3.getEditText();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPass.getText().toString();
                String newPassword = newPass.getText().toString();
                String confirmNewPassword = confirmPass.getText().toString();
                if (newPassword.equals(confirmNewPassword)) {
                    UserController.changePassword(ChangePasswordActivity.this, oldPassword, newPassword, new UserController.ChangePasswordCallback() {
                        @Override
                        public void onResponse(String status, String message) {
                            if (status.equals("success")) {
                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(String error) {
                            Toast.makeText(ChangePasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "New password and confirm password do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}