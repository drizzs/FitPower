package com.fitgroup.fitpower.login;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.utils.StaticVar;

import java.util.Date;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        StaticVar.selectedCalendarDate.setTime(new Date());

        activeActivity = R.layout.activity_title;

        Button signIn = findViewById(R.id.ttl_signin_btn);
        Button createAcc = findViewById(R.id.ttl_create_acc_btn);

        signIn.setOnClickListener(view -> {
            activeActivity = R.layout.activity_signin;
            startActivity(new Intent(this, SignInActivity.class));
        });

        createAcc.setOnClickListener(view -> {
            activeActivity = R.layout.activity_tos_signup;
            startActivity(new Intent(this, TOSActivity.class));
        });
    }
}
