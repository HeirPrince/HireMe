package com.nassaty.hireme.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.activities.AuthActivity;
import com.nassaty.hireme.listeners.ProfileListener;

public class AuthUtils {
    private Context context;
    private FirebaseAuth auth;
    private CollectionReference userRef;

    public AuthUtils(Context context) {
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
    }

    public boolean checkFirstUse(){
        FirebaseUserMetadata firebaseUserMetadata = auth.getCurrentUser().getMetadata();
        if (firebaseUserMetadata.getCreationTimestamp() == firebaseUserMetadata.getLastSignInTimestamp())
            return true;
        else
            return false;

    }

    public Boolean checkAuth(){
        if (auth.getCurrentUser() != null)return true;
        else return false;
    }

    public FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }

    public Boolean doSignOut(Context context){
        if (auth.getCurrentUser() != null){
            auth.signOut();
            context.startActivity(new Intent(context, AuthActivity.class));
            return true;
        }else {
            return false;
        }
    }

    public void doSignIn(Context context){
        if (auth.getCurrentUser() == null){
            context.startActivity(new Intent(context, AuthActivity.class));
        }else {
            doSignOut(context);
        }
    }

    public void checkRegister(String uid, final ProfileListener callback) {
        userRef = FirebaseFirestore.getInstance().collection("users");

        userRef.whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot.getDocuments().isEmpty()){
                            callback.isRegistered(false);
                        }else {
                            callback.isRegistered(true);
                        }
                    }
                });
    }

    public boolean isMine(String uid){
        if (uid.equals(auth.getCurrentUser().getUid()))
            return true;
        else
            return false;
    }

}
