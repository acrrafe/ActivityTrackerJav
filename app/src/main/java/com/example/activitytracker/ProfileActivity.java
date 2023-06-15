package com.example.activitytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.service.controls.templates.ControlTemplate;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private TextView getAct, getPending;
    private BarChart activitiesChart;
    private BottomNavigationView bottomNavigationView;
    private static ArrayList<String> daysOfWeeks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getPending = findViewById(R.id.getPending);
        getAct = findViewById(R.id.getAct);
        activitiesChart = findViewById(R.id.activitiesChart);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<Activities> activities = databaseHelper.getEveryone();
        ArrayList<ActivityEnd> completeDate = databaseHelper.getDates();
        ArrayList<String> getDate = new ArrayList<>();

        // Loop the number of complete activities
        int numAct = 0, numPend=0;
        for(int count=0; count<completeDate.size(); count++){
            getDate.add(completeDate.get(count).getDateEndAct());
            numAct = (completeDate.get(count).getNumAct());
            getAct.setText(String.valueOf(numAct));
        }
        // Loop for number of pending activities
        for(int count=0; count<=activities.size(); count++){
            numPend=count;
        }
        getPending.setText(String.valueOf(numPend));

        // Current Date
        DateFormat format = new SimpleDateFormat("MMM");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year);   // year
        calendar.set(Calendar.MONTH, 0); // month
        calendar.setFirstDayOfWeek(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        String[] days = new String[12]; // store each month in array
        for (int i = 0; i < 12; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
        }
        // Loop Completed Activities if they match the month
        int counter=0, actCount =0;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int countFirst=0; countFirst<12; countFirst++){
            counter=0;
            for(int count = 0; count<getDate.size(); count++){ //days of week
                if(getDate.get(count).contains(days[countFirst])){
                    counter++;
                }else {
                    System.out.println("No activity for this day");
                }
            }
            BarEntry barEntry = new BarEntry(actCount, counter); // x, y
            barEntries.add(barEntry);
            actCount++;

        }

        // Initialize Bar Data Set
        BarDataSet barDataSet = new BarDataSet(barEntries, "- Pending Activities");
        barDataSet.setDrawValues(false);
        int color, colorBar;
        // Check if Night Theme is on
        ThemeSwitcherActivity themeSwitcherActivity = new ThemeSwitcherActivity(ProfileActivity.this);
        SharedPreferences shared = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean isOn = shared.getBoolean("appTheme", themeSwitcherActivity.isOn());
        if(isOn){
            color = ContextCompat.getColor(ProfileActivity.this, R.color.white);
            colorBar = ContextCompat.getColor(ProfileActivity.this, R.color.yellowLight);
        }else {
            color = ContextCompat.getColor(ProfileActivity.this, R.color.black);
            colorBar = ContextCompat.getColor(ProfileActivity.this, R.color.yellowDark);
        }
        barDataSet.setValueTextColor(color);
        barDataSet.setColor(colorBar);
        // set the BarData to the chart
        activitiesChart.setData(new BarData(barDataSet));
        // animation time
        activitiesChart.animateXY(3000, 3000);
        // set description to false
        activitiesChart.getDescription().setEnabled(false);

        // Customize the X axis
        XAxis xAxis = activitiesChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(color);
        // Customize the Y axis
        YAxis yAxis = activitiesChart.getAxisRight();
        yAxis.setEnabled(false);
        yAxis.setGranularity(1);
        yAxis.setGranularityEnabled(true);
        // Customize Chart
        activitiesChart.setDragEnabled(true);
        activitiesChart.setVisibleXRangeMaximum(7);
        activitiesChart.getLegend().setEnabled(false);
        activitiesChart.getAxisLeft().setDrawGridLines(false);
        activitiesChart.getAxisRight().setDrawGridLines(false);
        activitiesChart.getXAxis().setDrawGridLines(false);
        activitiesChart.getAxisLeft().setTextColor(color);

        // Bottom Nav Bar if this activity is selected
        bottomNavigationView.setSelectedItemId(R.id.Profile);
        bottomNavigationView.setBackgroundResource(0);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.Profile:
                        return true;
                    case R.id.Setting:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }


}