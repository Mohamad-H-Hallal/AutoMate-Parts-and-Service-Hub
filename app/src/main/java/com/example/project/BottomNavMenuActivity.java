package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.project.databinding.ActivityBottomNavMenuBinding;
import com.example.project.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavMenuActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_scrap_yard);
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