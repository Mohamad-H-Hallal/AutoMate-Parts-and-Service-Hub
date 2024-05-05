package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

public class PaymentActivity extends AppCompatActivity {

    private ImageButton back;
    private ShapeableImageView omtViewPay,whishViewPay,omtViewPay2,whishViewPay2;
    private CardView omtCardView,whishCardView;
    private AppCompatButton omtPay,whishPay;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        back = findViewById(R.id.back_arrow10);
        omtViewPay = findViewById(R.id.omtViewPay);
        omtViewPay2 = findViewById(R.id.omtViewPay2);
        whishViewPay = findViewById(R.id.whishViewPay);
        whishViewPay2 = findViewById(R.id.whishViewPay2);
        omtCardView = findViewById(R.id.omtCardView);
        whishCardView = findViewById(R.id.whishCardView);
        omtPay = findViewById(R.id.omtPay);
        whishPay = findViewById(R.id.whishPay);
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
        omtViewPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                omtViewPay.setVisibility(View.GONE);
                omtViewPay2.setVisibility(View.VISIBLE);
                omtCardView.setVisibility(View.VISIBLE);
            }
        });
        omtViewPay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                omtViewPay2.setVisibility(View.GONE);
                omtViewPay.setVisibility(View.VISIBLE);
                omtCardView.setVisibility(View.GONE);
            }
        });
        whishViewPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishViewPay.setVisibility(View.GONE);
                whishViewPay2.setVisibility(View.VISIBLE);
                whishCardView.setVisibility(View.VISIBLE);
            }
        });
        whishViewPay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishViewPay2.setVisibility(View.GONE);
                whishViewPay.setVisibility(View.VISIBLE);
                whishCardView.setVisibility(View.GONE);
            }
        });
        omtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PaymentActivity.this, PayCompletionActivity.class),REQUEST_CODE);
            }
        });
        whishPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PaymentActivity.this, PayCompletionActivity.class),REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            finish();
        }
    }
}