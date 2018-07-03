package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.listeners.ProfileSetupListener;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.AuthVModel;

public class Register extends AppCompatActivity {

    private EditText username;
    AuthVModel authVModel;
    private AuthUtils authUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        authVModel = ViewModelProviders.of(this).get(AuthVModel.class);
        authUtils = new AuthUtils(this);

    }

    public void signUp(View view) {
        User user = new User();
        user.setUser_name(username.getText().toString());
        user.setPhone_number(authUtils.getCurrentUser().getPhoneNumber());
        user.setUID(authUtils.getCurrentUser().getUid());

        authVModel.setupProfile(user, new ProfileSetupListener() {
            @Override
            public void isProfileSet(Boolean isSet) {
                if (isSet){
                    Toast.makeText(Register.this, "profile set", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Register.this, MainActivity.class));
                }else {
                    Toast.makeText(Register.this, "profile not set", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
