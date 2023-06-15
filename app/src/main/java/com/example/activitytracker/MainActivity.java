package com.example.activitytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private RecyclerView activityRecView;
    TaskActivityRecView adapter;
    private FloatingActionButton fab;
    private static final int LAUNCH_ACTIVITY_FORM = 0;
    private BottomNavigationView bottomNavigationView;
    private ConstraintLayout constraintLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);

        ThemeSwitcherActivity themeSwitcherActivity = new ThemeSwitcherActivity(MainActivity.this);
        SharedPreferences shared = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean isOn = shared.getBoolean("appTheme", themeSwitcherActivity.isOn());
        System.out.println(isOn);
        if(isOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        constraintLayout = findViewById(R.id.constraintLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setBackgroundResource(0);
        ShowEveryone();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityForm.class);
                startActivityForResult(intent, LAUNCH_ACTIVITY_FORM);

            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Setting:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        ArrayList<String> arrayList = new ArrayList<>();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_ACTIVITY_FORM) {
            if(resultCode == ActivityForm.RESULT_OK){
             ShowEveryone();
            }
        }
    }
    public void ShowEveryone(){

        // TODO: Make a database that will show everyone in the database/arraylist
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        ArrayList<Activities> activity = databaseHelper.getEveryone();
        Collections.sort(activity, new ActivitiesComparator()); // Use Collection to sort activity by calling ActivitiesComparator

        adapter = new TaskActivityRecView(this); // pass the context of Main Activity to the adapter
        activityRecView = findViewById(R.id.activityRecView); // Initialize the Recycler View of this activity
        activityRecView.setAdapter(adapter); // set the adapter of this activity's Recycler view as the adapter
        activityRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); // initialize a layout of the recycler view
        adapter.setActivity(activity); // the adapter of StudetsRecViewAdapter must get the students ArrayList
    }
}