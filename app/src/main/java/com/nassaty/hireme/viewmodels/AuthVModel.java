package com.nassaty.hireme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.nassaty.hireme.activities.MainActivity;
import com.nassaty.hireme.listeners.ProfileListener;
import com.nassaty.hireme.listeners.ProfileSetupListener;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;

public class AuthVModel extends AndroidViewModel {

    CollectionReference userRef;
    AuthUtils authUtils;

    public AuthVModel(@NonNull Application application) {
        super(application);
        this.authUtils = new AuthUtils(application);
    }

    public void setupProfile(final User user, final ProfileSetupListener listener){

        if (authUtils.checkAuth())
        {
            authUtils.registerUser(user, new ProfileListener() {
                @Override
                public void isRegistered(Boolean state) {
                    if (state){
                        getApplication().startActivity(new Intent(getApplication(), MainActivity.class));
                    }else {
                        Toast.makeText(getApplication(), "User not registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
