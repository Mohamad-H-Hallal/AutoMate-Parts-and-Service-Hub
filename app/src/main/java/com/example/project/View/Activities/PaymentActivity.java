package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.google.android.material.imageview.ShapeableImageView;

public class PaymentActivity extends BaseActivity {

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
        String selectedLanguage = preferences.getString("selected_language", "en");
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
                Intent i = new Intent(PaymentActivity.this, PayCompletionActivity.class);
                i.putExtra("type","omt");
                startActivityForResult(i,REQUEST_CODE);
            }
        });
        whishPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivity.this, PayCompletionActivity.class);
                i.putExtra("type","whish");
                startActivityForResult(i,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Intent i = new Intent();
            setResult(RESULT_OK,i);
            finish();
        }
    }
}