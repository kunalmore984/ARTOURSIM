package com.example.ar_tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        //loading the default fragment
        loadFragment(new HomeFragment(),
                "HOME");

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }
    private boolean loadFragment(Fragment fragment,String title) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            getSupportActionBar().setTitle(title);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title="";
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();
                title= "HOME";
                break;

            case R.id.explore:
                fragment = new MapsFragment();
                title= "MAPS";
                break;

            case R.id.settings:
                fragment = new settings();
                title= "SETTINGS";
                break;

            case R.id.trips:
                fragment = new Trip();
                title= "TRIPS";
                break;
        }

        return loadFragment(fragment,title);
    }
}