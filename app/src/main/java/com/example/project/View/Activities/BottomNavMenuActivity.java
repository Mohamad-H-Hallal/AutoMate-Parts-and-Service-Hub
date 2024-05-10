package com.example.project.View.Activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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
    int def_val = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Intent i = getIntent();
        def_val = i.getIntExtra("start_page",0);
        if(def_val == 0)
            bottomNavigationView.setSelectedItemId(R.id.nav_scrap_yard);
        else
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
    ScrapYardFragment firstFragment = new ScrapYardFragment();
    PartsFragment secondFragment = new PartsFragment();
    MechanicFragment thirdFragment = new MechanicFragment();
    DiagnosticsFragment fourthFragment = new DiagnosticsFragment();
    ProfileFragment fifthFragment = new ProfileFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId()==R.id.nav_scrap_yard) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, firstFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.nav_parts) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, secondFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.nav_mechanics){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, thirdFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.nav_diagnostics){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fourthFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.nav_profile){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fifthFragment)
                        .commit();
                return true;
            }
        return false;
    }
}