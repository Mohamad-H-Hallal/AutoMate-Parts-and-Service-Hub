package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.Controller.UserController;
import com.example.project.Controller.UserData;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

public class PayCompletionActivity extends BaseActivity {

    private ImageButton back;
    private AppCompatButton paymentSubmit;
    private AlertDialog Dialog;
    private String type;
    private TextInputLayout textInputLTN, textInputFN, textInputLN;
    private EditText LTN, FN, LN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_completion);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        back = findViewById(R.id.back_arrow11);
        textInputLTN = findViewById(R.id.textInputLTN);
        textInputFN = findViewById(R.id.textInputFN);
        textInputLN = findViewById(R.id.textInputLN);
        LTN = textInputLTN.getEditText();
        FN = textInputFN.getEditText();
        LN = textInputLN.getEditText();
        paymentSubmit = findViewById(R.id.paymentSubmit);
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
        LTN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.matches("^\\d{4}-\\d{4}-\\d{4}$")) {
                    LTN.setError("Invalid format. Please use XXXX-XXXX-XXXX");
                    paymentSubmit.setEnabled(false);
                } else {
                    LTN.setError(null);
                    paymentSubmit.setEnabled(true);
                }
            }
        });
        paymentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = FN.getText().toString();
                String lastName = LN.getText().toString();
                String ltn = LTN.getText().toString();
                if (!(firstName.isEmpty() || lastName.isEmpty() || ltn.isEmpty())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayCompletionActivity.this);
                    View DialogView = LayoutInflater.from(PayCompletionActivity.this).inflate(R.layout.complete_payment_dialog, null);
                    final AppCompatButton yesButton = DialogView.findViewById(R.id.cp_yes_button);
                    final AppCompatButton noButton = DialogView.findViewById(R.id.cp_no_button);
                    builder.setView(DialogView);
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent resultIntent = new Intent();
                            type = getIntent().getStringExtra("type");
                            UserController.submitPaymentDetails(PayCompletionActivity.this, firstName, lastName, ltn, type, new UserController.PaymentCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    Intent intent = new Intent(PayCompletionActivity.this, BottomNavMenuActivity.class);
                                    setResult(RESULT_OK, resultIntent);
                                    Toast.makeText(PayCompletionActivity.this, response, Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PayCompletionActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                            if (Dialog != null && Dialog.isShowing()) {
                                Dialog.dismiss();
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
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Dialog.show();
                } else {
                    Toast.makeText(PayCompletionActivity.this, "Enter the needed information!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}