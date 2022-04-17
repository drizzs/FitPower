package com.fitgroup.fitpower.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.ConnectiveActivity;

public class ConfirmationActivity extends ConnectiveActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        EditText code = findViewById(R.id.email_confirmation_code_field);
        Button validate = findViewById(R.id.email_confirmation_validate_btn);
        TextView resend = findViewById(R.id.email_confirmation_resend);




    }
}
