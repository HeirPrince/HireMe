package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.nassaty.hireme.listeners.ProfileListener;
import com.nassaty.hireme.utils.AuthUtils;

import java.util.Arrays;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth auth;
    private AuthUtils authUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        auth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils(this);

        if (authUtils.checkAuth()){
            checkRegistration();
        }else {
            setupSignIn();
        }


    }

    private void setupSignIn() {

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("rw").build()
            ))
                .setTosUrl("http://www.google.com")
                .setPrivacyPolicyUrl("http://www.google.com")
            .build(), RC_SIGN_IN);
    }

    public void checkRegistration(){
        authUtils.checkRegister(authUtils.getCurrentUser().getPhoneNumber(), new ProfileListener() {
            @Override
            public void isRegistered(Boolean state) {
                if (!state){
                    startActivity(new Intent(AuthActivity.this, Register.class));
                    finish();
                }else {
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK){
                checkRegistration();
            }else {
                if(response == null){
                    Toast.makeText(this, "unknown error occurred", Toast.LENGTH_SHORT).show();
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this, "network not found", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(this, "Sign in error : "+response.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
