package com.example.project.View.Activities;

import android.app.AlertDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.Controller.UserController;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LoginActivity extends BaseActivity implements UserController.AuthenticationCallback {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AlertDialog Dialog;
    private static final int REQUEST_CODE = 1;

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
    public void onSuccess(int id, String accountType, String endDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        try {
            if (endDate != null) {
                Date parsedEndDate = dateFormat.parse(endDate);
                Date currentDateObject = dateFormat.parse(currentDate);
                if (parsedEndDate.before(currentDateObject) || parsedEndDate.equals(currentDateObject)) {
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    showUpgradePrompt();
                } else {
                    startActivity(new Intent(this, BottomNavMenuActivity.class));
                    finish();
                }
            } else {
                startActivity(new Intent(this, BottomNavMenuActivity.class));
                finish();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void showUpgradePrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View DialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.payment_warning_dialog, null);
        final AppCompatButton upgradeButton = DialogView.findViewById(R.id.upgradeNow);
        final AppCompatButton cancelButton = DialogView.findViewById(R.id.cancelUpgrade);
        builder.setView(DialogView);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PaymentActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                if (Dialog != null && Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Dialog != null && Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            }
        });
        Dialog = builder.create();
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dialog.show();
    }


    public void onRegisterClick(View View) {
        editTextEmail.setText("");
        editTextPassword.setText("");
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void resetPassword(View view) {
        Intent resetPass = new Intent(this, PasswordResetActivity.class);
        startActivity(resetPass);
        finish();
    }

    private String getLanguagePreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("selected_language", "en");
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            finish();
        }
    }

}