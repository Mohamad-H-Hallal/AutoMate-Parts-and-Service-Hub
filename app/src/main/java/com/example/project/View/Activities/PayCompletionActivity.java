package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.R;

public class PayCompletionActivity extends BaseActivity {

    private ImageButton back;
    private AppCompatButton paymentSubmit;
    private AlertDialog Dialog;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_completion);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        back = findViewById(R.id.back_arrow11);
        paymentSubmit = findViewById(R.id.paymentSubmit);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            back.setImageResource(R.drawable.ic_back_ar);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        paymentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PayCompletionActivity.this);
                View DialogView = LayoutInflater.from(PayCompletionActivity.this).inflate(R.layout.complete_payment_dialog, null);
                final AppCompatButton yesButton = DialogView.findViewById(R.id.cp_yes_button);
                final AppCompatButton noButton = DialogView.findViewById(R.id.cp_no_button);
                builder.setView(DialogView);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PayCompletionActivity.this, BottomNavMenuActivity.class);
                        Intent resultIntent = new Intent();
                        type = getIntent().getStringExtra("type");
                        setResult(RESULT_OK, resultIntent);
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog = builder.create();
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Dialog.show();
            }
        });
    }
}