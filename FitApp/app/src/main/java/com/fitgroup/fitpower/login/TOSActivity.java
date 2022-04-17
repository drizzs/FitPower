package com.fitgroup.fitpower.login;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.ConnectiveActivity;

public class TOSActivity extends ConnectiveActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos_signup);

        Button cont = findViewById(R.id.tos_continue);
        CheckBox agree  = findViewById(R.id.tos_accept);
        Button back = findViewById(R.id.tos_back);

        back.setOnClickListener((view)->{
            activeActivity = R.layout.activity_title;
            startActivity(new Intent(this, TitleActivity.class));
        });

        cont.setOnClickListener((view)->{
            if(agree.isChecked()) {
                activeActivity = R.layout.activity_create_acc;
                startActivity(new Intent(this, CreateAccActivity.class));
            }
        });

    }


}
