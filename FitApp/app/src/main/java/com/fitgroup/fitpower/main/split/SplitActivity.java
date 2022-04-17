package com.fitgroup.fitpower.main.split;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

public class SplitActivity extends NavActivity {

    public SplitActivity() {
        super(R.id.split_drawer, R.id.split_nav,R.layout.activity_split);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button createNew = findViewById(R.id.split_main_crt_btn);

        createNew.setOnClickListener(view -> openNewSplitActivity());


    }

    private void openNewSplitActivity(){
        activeActivity = R.layout.activity_split_new;
        startActivity(new Intent(this, NewSplitActivity.class));
    }

}
