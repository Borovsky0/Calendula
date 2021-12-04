package com.bvl.calendula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.bvl.calendula.ui.DayFragment;
import com.bvl.calendula.ui.WeekFragment;
import com.bvl.calendula.ui.MonthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    Calendar todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todayDate = Calendar.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        if(selectedFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DayFragment(todayDate)).commit();
        }
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
                        case R.id.week:
                            selectedFragment = new WeekFragment();
                            break;
                        case R.id.year:
                            selectedFragment = new MonthFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}