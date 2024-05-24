package com.example.project.View.Activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.project.R;
import com.example.project.View.Fragments.DiagnosticsFragment;
import com.example.project.View.Fragments.MechanicFragment;
import com.example.project.View.Fragments.PartsFragment;
import com.example.project.View.Fragments.ProfileFragment;
import com.example.project.View.Fragments.ScrapYardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavMenuActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private int selectedItemId = R.id.nav_scrap_yard;
    private static final String SELECTED_ITEM_ID_KEY = "selected_item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Intent i = getIntent();
        int def_val = i.getIntExtra("start_page",0);
        if(def_val == 0)
            bottomNavigationView.setSelectedItemId(R.id.nav_scrap_yard);
        else
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);


        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(SELECTED_ITEM_ID_KEY);
        }

        bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID_KEY, selectedItemId);
    }

    ScrapYardFragment firstFragment = new ScrapYardFragment();
    PartsFragment secondFragment = new PartsFragment();
    MechanicFragment thirdFragment = new MechanicFragment();
    DiagnosticsFragment fourthFragment = new DiagnosticsFragment();
    ProfileFragment fifthFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_scrap_yard) {
            selectedFragment = firstFragment;
        } else if (item.getItemId() == R.id.nav_parts) {
            selectedFragment = secondFragment;
        } else if (item.getItemId() == R.id.nav_mechanics) {
            selectedFragment = thirdFragment;
        } else if (item.getItemId() == R.id.nav_diagnostics) {
            selectedFragment = fourthFragment;
        } else if (item.getItemId() == R.id.nav_profile) {
            selectedFragment = fifthFragment;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            selectedItemId = item.getItemId();
            return true;
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Add onDestroy logic here if needed
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Add onStop logic here if needed
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Add onResume logic here if needed
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Add onPause logic here if needed
    }

    }
