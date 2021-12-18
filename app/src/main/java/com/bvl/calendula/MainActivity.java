package com.bvl.calendula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bvl.calendula.ui.DayFragment;
import com.bvl.calendula.ui.MonthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    Calendar todayDate;

    TextView date;
    ImageView button_settings, button_theme, button_language;
    Animation open, close, rotateF, rotateB;
    boolean settingsOpen = false;

    String [] themeList, monthNamesRU;
    int theme = 0; //0 - light theme, 1 - dark theme, 2 - device theme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthNamesRU = getResources().getStringArray(R.array.month_names_ru);

        todayDate = Calendar.getInstance();

        date = findViewById(R.id.date);
        button_settings = findViewById(R.id.button_settings);
        button_theme = findViewById(R.id.button_theme);
        button_language = findViewById(R.id.button_language);

        date.setSelected(true);

        open = AnimationUtils.loadAnimation(this, R.anim.settings_open);
        close = AnimationUtils.loadAnimation(this, R.anim.settings_close);
        rotateF = AnimationUtils.loadAnimation(this, R.anim.settings_rotate_forward);
        rotateB = AnimationUtils.loadAnimation(this, R.anim.settings_rotate_backward);

        SharedPreferences appSettings = getSharedPreferences("AppSettings", 0);
        SharedPreferences.Editor appSettingsEditor = appSettings.edit();
        theme = appSettings.getInt("Theme", 2);
        setTheme(theme, appSettingsEditor, button_theme);

        setToolbarDay(todayDate);

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate();
            }
        });

        themeList = getResources().getStringArray(R.array.theme);

        button_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialogTheme);
                builder.setTitle(getString(R.string.theme_choose));
                builder.setSingleChoiceItems(themeList, theme, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        theme = i;
                    }
                });

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTheme(theme, appSettingsEditor, button_theme);
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                animate();
            }
        });

        button_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        if(selectedFragment == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, DayFragment.newInstance(todayDate)).commit();
        }
    }

    public void setTheme(int theme, SharedPreferences.Editor editor, ImageView button)
    {
        switch (theme)
        {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putInt("Theme", 0);
                editor.apply();
                button.setImageResource(R.drawable.ic_theme_dark);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putInt("Theme", 1);
                editor.apply();
                button.setImageResource(R.drawable.ic_theme_light);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.putInt("Theme", 2);
                editor.apply();

                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        button.setImageResource(R.drawable.ic_theme_dark);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        button.setImageResource(R.drawable.ic_theme_light);
                        break;
                }

                break;
        }
    }

    private void animate()
    {
        if(settingsOpen)
        {
            button_settings.startAnimation(rotateF);
            button_theme.startAnimation(close);
            button_language.startAnimation(close);
            button_theme.setClickable(false);
            button_language.setClickable(false);
            settingsOpen = false;
        }
        else
        {
            button_settings.startAnimation(rotateB);
            button_theme.startAnimation(open);
            button_language.startAnimation(open);
            button_theme.setClickable(true);
            button_language.setClickable(true);
            settingsOpen = true;
        }
    }

    public void setToolbarDay(Calendar cal)
    {
        Calendar calT = Calendar.getInstance(); //today
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.today) + new SimpleDateFormat(", E").format(cal.getTime()));
            return; }

        calT.add(Calendar.DATE, 1); // tomorrow
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.tomorrow) + new SimpleDateFormat(", E").format(cal.getTime()));
            return; }

        calT.add(Calendar.DATE, -2); //yesterday
        if(cal.get(Calendar.DAY_OF_YEAR) == calT.get(Calendar.DAY_OF_YEAR) &&
                cal.get(Calendar.YEAR) == calT.get(Calendar.YEAR)) {
            date.setText(getString(R.string.yesterday) + new SimpleDateFormat(", E").format(cal.getTime()));
            return; }

        date.setText(new SimpleDateFormat("d MMMM, E").format(cal.getTime()));
    }

    public void setToolbarMonth(Calendar cal)
    {
        if(Locale.getDefault().getLanguage() == "ru") {
            date.setText(monthNamesRU[cal.get(Calendar.MONTH)] + ", " + Integer.toString(cal.get(Calendar.YEAR)));
        }
        else {
            date.setText(new SimpleDateFormat("MMMM, yyyy").format(cal.getTime()));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.day:
                            setToolbarDay(todayDate);
                            selectedFragment = DayFragment.newInstance(todayDate);
                            break;
                        case R.id.month:
                            setToolbarMonth(todayDate);
                            selectedFragment = MonthFragment.newInstance(todayDate);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}