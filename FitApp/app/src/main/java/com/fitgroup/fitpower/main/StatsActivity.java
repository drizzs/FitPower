package com.fitgroup.fitpower.main;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

public class StatsActivity extends NavActivity {

    public StatsActivity() {
        super(R.id.stats_drawer, R.id.stats_nav,R.layout.activity_stats);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
