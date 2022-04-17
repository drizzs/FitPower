package com.fitgroup.fitpower.main.split;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

import java.util.ArrayList;
import java.util.List;

public class NewSplitActivity extends NavActivity {

    private Spinner days;
    private Spinner times;

    private int totalDays;
    private int timesPerDay;

    private LinearLayout splitLayout;

    public NewSplitActivity() {
        super(R.id.split_new_drawer, R.id.split_new_nav,R.layout.activity_split_new);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        days = findViewById(R.id.split_days_spin);
        times = findViewById(R.id.split_times_spin);
        splitLayout = findViewById(R.id.split_add_layout);

        setSpinners();

        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String daysS = adapterView.getItemAtPosition(i).toString();

                if(!daysS.equals("")) {
                    totalDays = Integer.parseInt(adapterView.getItemAtPosition(i).toString());

                    if(times.getSelectedItem().toString().equals("")){
                        times.setSelection(1);
                    }

                }else{
                    totalDays = 0;
                }
                setSplitScroll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String timesS = adapterView.getItemAtPosition(i).toString();

                if(!timesS.equals("")) {
                    timesPerDay = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                }else{
                    timesPerDay = 0;
                }
                setSplitScroll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSplitScroll(){
        splitLayout.removeAllViews();
        if(totalDays > 0 && timesPerDay > 0){
            for(int i = 1; i <= totalDays;++i) {
                splitLayout.addView(addSplitDay(i));
            }
        }
    }

    private CardView addSplitDay(int day){
        CardView view = new CardView(this);

        LinearLayout mainLayout = getLinearLayout(this,LinearLayout.VERTICAL);

        TextView label = getTextView(this,"Day "+ day ,24,50,100,10,10,10,10, Gravity.CENTER_HORIZONTAL);

        mainLayout.addView(label);

        for(int i = 1; timesPerDay >= i;++i){
            TextView workout;
            if(timesPerDay > 1) {
                workout = getTextView(this,"Routine "+ i ,16,50,150,10,10,10,10, Gravity.CENTER_HORIZONTAL);

            } else{
                workout = getTextView(this,"Routine" ,16,50,130,10,10,10,10, Gravity.CENTER_HORIZONTAL);
            }
            mainLayout.addView(workout);
            mainLayout.addView(addRoutineCard(i));
        }

        view.addView(mainLayout);
        return view;
    }

    private CardView addRoutineCard(int time){
        CardView view = new CardView(this);


        return view;
    }


    private void setSpinners(){
        List<String> list = new ArrayList<>();

        list.add("");
        for(int i = 1 ; i <= 7; ++i){
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);

        days.setAdapter(arrayAdapter);

        list = new ArrayList<>();

        list.add("");

        for(int i = 1 ; i <= 4; ++i){
            list.add(String.valueOf(i));
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);

        times.setAdapter(arrayAdapter);
    }


}
