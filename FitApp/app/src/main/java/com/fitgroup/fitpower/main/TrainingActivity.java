package com.fitgroup.fitpower.main;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

public class TrainingActivity extends NavActivity {

    public TrainingActivity() {
        super(R.id.training_drawer, R.id.training_nav,R.layout.activity_training);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
