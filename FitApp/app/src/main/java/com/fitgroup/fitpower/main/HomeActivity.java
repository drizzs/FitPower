package com.fitgroup.fitpower.main;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

public class HomeActivity extends NavActivity {

    public HomeActivity() {
        super(R.id.main_drawer, R.id.main_nav,R.layout.activity_main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(activeActivity == 0){
            activeActivity = R.layout.activity_main;
        }
        super.onCreate(savedInstanceState);
    }
}
