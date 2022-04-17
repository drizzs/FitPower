package com.fitgroup.fitpower.login;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignInActivity extends ConnectiveActivity {

    public SharedPreferences MyPrefs;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button signIn = findViewById(R.id.sign_in_btn_login);

        username = findViewById(R.id.sign_in_username_login);
        password = findViewById(R.id.sign_in_password_login);

        MyPrefs = getApplication().getSharedPreferences("MyPrefs",MODE_PRIVATE);

        if(MyPrefs.getString("Username",null) != null && MyPrefs.getString("Password",null) != null){

            username.setText(MyPrefs.getString("Username",null));
            password.setText(MyPrefs.getString("Password",null));

            JSONObject json = new JSONObject();
            try {
                json.put("Username",username.getText());
                json.put("RawPassword",password.getText());

                getObjectFromPost("https://fitrickapi.azurewebsites.net/login",json,1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        signIn.setOnClickListener((view -> {

             JSONObject json = new JSONObject();
            try {
                json.put("Username",username.getText());
                json.put("RawPassword",password.getText());

                getObjectFromPost("https://fitrickapi.azurewebsites.net/login/login",json,1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }));
    }

    @Override
    protected void responsePostHandler(JSONObject obj,int handler) {
        if (obj != null) {
            SignInActivity.this.runOnUiThread(() -> {
                try {
                    if (obj.getString("Answer").equals("Success")) {
                        StaticVar.hashedPassword = obj.getString("HashedPassword");
                        StaticVar.accountInfoSID = obj.getInt("AccountInfoSID");

                        MyPrefs.edit().putString("Username",username.getText().toString()).apply();
                        MyPrefs.edit().putString("Password",password.getText().toString()).apply();

                        activeActivity = R.layout.activity_main;
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
