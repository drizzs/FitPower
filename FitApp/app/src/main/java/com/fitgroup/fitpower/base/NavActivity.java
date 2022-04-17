package com.fitgroup.fitpower.base;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.login.TitleActivity;
import com.fitgroup.fitpower.main.calendar.CalendarActivity;
import com.fitgroup.fitpower.main.HomeActivity;
import com.fitgroup.fitpower.main.routine.RoutineActivity;
import com.fitgroup.fitpower.main.ScienceActivity;
import com.fitgroup.fitpower.main.SettingsActivity;
import com.fitgroup.fitpower.main.split.SplitActivity;
import com.fitgroup.fitpower.main.StatsActivity;
import com.fitgroup.fitpower.main.TrainingActivity;
import com.fitgroup.fitpower.main.workout.WorkoutMainActivity;
import com.fitgroup.fitpower.main.gym.GymMainActivity;
import com.google.android.material.navigation.NavigationView;

public class NavActivity extends ConnectiveActivity implements NavigationView.OnNavigationItemSelectedListener {

    public SharedPreferences myPrefs;

    protected DrawerLayout drawer;

    private final int drawerID;
    private final int navViewID;
    private final int activityID;

    public NavActivity(int drawerID,int navViewID,int activityID){
        this.drawerID = drawerID;
        this.navViewID = navViewID;
        this.activityID = activityID;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityID);

        initiateNav();

        initiateFields();
    }

    protected void initiateNav(){
        myPrefs = getApplication().getSharedPreferences("MyPrefs",MODE_PRIVATE);

        drawer = findViewById(drawerID);

        NavigationView navView = findViewById(navViewID);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle t = new ActionBarDrawerToggle(this,drawer,null,R.string.nav_drawer_open,R.string.nav_drawer_close);

        drawer.addDrawerListener(t);

        t.syncState();
    }

    protected void initiateFields(){

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home_main:
                if(activeActivity != R.layout.activity_main){
                    activeActivity = R.layout.activity_main;
                    startActivity(new Intent(this, HomeActivity.class));
                }
                break;
            case R.id.nav_gym_main:
                if(activeActivity != R.layout.activity_gym){
                    activeActivity = R.layout.activity_gym;
                    startActivity(new Intent(this, GymMainActivity.class));
                }
                break;
            case R.id.nav_workouts_main:
                if(activeActivity != R.layout.activity_workout){
                    activeActivity = R.layout.activity_workout;
                    startActivity(new Intent(this, WorkoutMainActivity.class));
                }
                break;
            case R.id.nav_routines_main:
                if(activeActivity != R.layout.activity_routine){
                    activeActivity = R.layout.activity_routine;
                    startActivity(new Intent(this, RoutineActivity.class));
                }
                break;
            case R.id.nav_split_main:
                if(activeActivity != R.layout.activity_split){
                    activeActivity = R.layout.activity_split;
                    startActivity(new Intent(this, SplitActivity.class));
                }
                break;
            case R.id.nav_calendar_main:
                if(activeActivity != R.layout.activity_calendar){
                    activeActivity = R.layout.activity_calendar;
                    startActivity(new Intent(this, CalendarActivity.class));
                }
                break;
            case R.id.nav_stats_main:
                if(activeActivity != R.layout.activity_stats){
                    activeActivity = R.layout.activity_stats;
                    startActivity(new Intent(this, StatsActivity.class));
                }
                break;
            case R.id.nav_training_main:
                if(activeActivity != R.layout.activity_training){
                    activeActivity = R.layout.activity_training;
                    startActivity(new Intent(this, TrainingActivity.class));
                }
                break;
            case R.id.nav_science_main:
                if(activeActivity != R.layout.activity_science){
                    activeActivity = R.layout.activity_science;
                    startActivity(new Intent(this, ScienceActivity.class));
                }
                break;
            case R.id.nav_settings_main:
                if(activeActivity != R.layout.activity_settings){
                    activeActivity = R.layout.activity_settings;
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                break;
            case R.id.nav_logout_main:
                if(activeActivity != R.layout.activity_title){

                    myPrefs.edit().putString("Username", null).apply();
                    myPrefs.edit().putString("Password", null).apply();

                    activeActivity = R.layout.activity_title;
                    startActivity(new Intent(this, TitleActivity.class));
                }
                break;
        }

        return false;
    }

    protected void setVisible(View view){
        view.setVisibility(View.VISIBLE);
    }

    protected void setInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }

    protected void setBackGroundColor(View view,int color){
        view.setBackgroundColor(getResources().getColor(color));
    }

}
