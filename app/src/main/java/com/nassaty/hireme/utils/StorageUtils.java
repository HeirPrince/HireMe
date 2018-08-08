package com.nassaty.hireme.utils;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;

public class StorageUtils {

    private FirebaseStorage firebaseStorage;
    private Context context;

    public StorageUtils(Context context, FirebaseStorage firebaseStorage) {
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    public void uploadImage(){

    }
}
