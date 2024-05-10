package com.example.project.View.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.preference.PreferenceManager;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        applyLanguageChange(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update language on resume to handle configuration changes
        applyLanguageChange(requireContext());
    }

    private void applyLanguageChange(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString("selected_language", "");

        // If no language preference is set, use the default system language
        if (language.isEmpty()) {
            language = Locale.getDefault().getLanguage();
        }

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
