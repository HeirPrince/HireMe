package com.nassaty.hireme.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.listeners.ProfileSetupListener;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.AuthVModel;

public class Register extends AppCompatActivity {

    private EditText username, email;
    private Button register;
    AuthVModel authVModel;
    private AuthUtils authUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);
        register = findViewById(R.id.register);

        authVModel = ViewModelProviders.of(this).get(AuthVModel.class);
        authUtils = new AuthUtils(this);

    }

    public void signUp(View view) {
        if (validate()){
            register.setEnabled(true);
            User user = new User();
            user.setUser_name(username.getText().toString());
            user.setPhone_number(authUtils.getCurrentUser().getPhoneNumber());
            user.setUID(authUtils.getCurrentUser().getUid());

            showProgress(true);
            authVModel.setupProfile(user, new ProfileSetupListener() {
                @Override
                public void isProfileSet(Boolean isSet) {
                    if (isSet){
                        showProgress(false);
                        onSignUpSuccess();
                    }else {
                        showProgress(false);
                        onSignUpFailed();
                    }
                }
            });
        }else {
            showProgress(false);
            register.setEnabled(false);
        }
    }

    public void showProgress(Boolean isShowing){
        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        if (isShowing){
            progressDialog.show();
        }else {
            progressDialog.hide();
        }
    }

    public void onSignUpSuccess(){
        register.setEnabled(false);
        Toast.makeText(Register.this, "profile set", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(Register.this, MainActivity.class));
    }

    public void onSignUpFailed(){
        register.setEnabled(true);
        Toast.makeText(Register.this, "profile not set", Toast.LENGTH_SHORT).show();
    }

    public boolean validate(){
        boolean valid = true;

        String mail = email.getText().toString();
        String uname = username.getText().toString();

        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Enter a valid email address");
            valid = false;
        }else {
            email.setError(null);
        }

        if (uname.isEmpty()){
            username.setError("Username required");
            valid = false;
        }else {
            username.setError(null);
        }

        return valid;
    }

}
