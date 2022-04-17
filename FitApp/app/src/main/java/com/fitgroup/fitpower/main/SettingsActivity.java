package com.fitgroup.fitpower.main;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

public class SettingsActivity extends NavActivity {

    public SettingsActivity() {
        super(R.id.settings_drawer, R.id.settings_nav,R.layout.activity_settings);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
