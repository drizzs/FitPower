package com.fitgroup.fitpower.login;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.ConnectiveActivity;
import com.fitgroup.fitpower.main.HomeActivity;
import com.fitgroup.fitpower.utils.StaticVar;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccActivity extends ConnectiveActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        Button createAccBtn = findViewById(R.id.create_acc_create_btn);

        EditText firstName = findViewById(R.id.create_acc_fn_field);
        EditText lastName = findViewById(R.id.create_acc_ln_field);

        EditText email = findViewById(R.id.create_acc_em_field);
        EditText confirmedEmail = findViewById(R.id.create_acc_cem_field);

        EditText password = findViewById(R.id.create_acc_pw_field);
        EditText confirmedPassword = findViewById(R.id.create_acc_cpw_field);

        Button back = findViewById(R.id.create_acc_back);

        back.setOnClickListener((view)->{
            activeActivity = R.layout.activity_title;
            startActivity(new Intent(this, TitleActivity.class));
        });

        createAccBtn.setOnClickListener((view)->{

            int target = 0;

            if(firstName.getText() == null){
                target = 1;
            }
            if(lastName.getText() == null){
                target = 1;
            }
            if(email.getText() == null){
                target = 1;
            }
            if(confirmedEmail.getText() == null){
                target = 1;
            }
            if(confirmedEmail.getText() != null && email.getText() != null) {
                if (confirmedEmail.getText() != email.getText()) {
                    target = 1;
                }
            }
            if(password.getText() == null){
                target = 1;
            }
            if(confirmedPassword.getText() == null){
                target = 1;
            }
            if(confirmedPassword.getText() != null && password.getText() != null) {
                if (confirmedPassword.getText() != password.getText()) {
                    target = 1;
                }
            }
            if(target == 0){
                JSONObject json = new JSONObject();
                try {
                    json.put("FirstName", firstName.getText());
                    json.put("LastName", lastName.getText());
                    json.put("Email", email.getText());
                    json.put("RawPassword", password.getText());
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                getObjectFromPost("https://fitrickapi.azurewebsites.net/insert/create_acc",json,1);

            }
        });
    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if (obj != null) {
            CreateAccActivity.this.runOnUiThread(() -> {
                try {
                    if (obj.getString("Answer").equals("Success")) {
                        StaticVar.hashedPassword = obj.getString("HashedPassword");
                        StaticVar.accountInfoSID = obj.getInt("AccountInfoSID");

                        activeActivity = R.layout.activity_gym;
                        startActivity(new Intent(CreateAccActivity.this, HomeActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
