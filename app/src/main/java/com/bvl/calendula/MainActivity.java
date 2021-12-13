package com.bvl.calendula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bvl.calendula.ui.DayFragment;
import com.bvl.calendula.ui.MonthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    Calendar todayDate;
    View toolbar;
    TextView date;
    ImageView button_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences appSettings = getSharedPreferences("AppSettingPrefs", 0);
        SharedPreferences.Editor appSettingsEditor = appSettings.edit();
        boolean nightMode = appSettings.getBoolean("NightMode", false);

        todayDate = Calendar.getInstance();

        toolbar = findViewById(R.id.toolbar);
        date = toolbar.findViewById(R.id.date);
        button_theme = toolbar.findViewById(R.id.button_theme);

        button_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        setToolbarDate(todayDate);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        if(selectedFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DayFragment(todayDate)).commit();
        }
    }

    public void setToolbarDate(Calendar cal)
    {
        Calendar calT = Calendar.getInstance(); //today
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.today) + new SimpleDateFormat(", EEEE").format(cal.getTime()));
            return; }

        calT.add(Calendar.DATE, 1); // tomorrow
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.tomorrow) + new SimpleDateFormat(", EEEE").format(cal.getTime()));
            return; }

        calT.add(Calendar.DATE, -2); //yesterday
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.yesterday) + new SimpleDateFormat(", EEEE").format(cal.getTime()));
            return; }

        date.setText(new SimpleDateFormat("d MMMM, EEEE").format(cal.getTime()));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.day:
                            selectedFragment = new DayFragment(todayDate);
                            break;
                        case R.id.month:
                            selectedFragment = new MonthFragment(todayDate);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}