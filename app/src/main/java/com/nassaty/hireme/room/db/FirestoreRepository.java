package com.nassaty.hireme.room.db;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public abstract class FirestoreRepository<Model> {

    protected FirebaseFirestore firebaseFirestore;
    protected CollectionReference collectionReference;
    protected FirestoreRepository<Model> firestoreRepositoryCallback;

    protected abstract String getRootNode();

    public FirestoreRepository(CollectionReference collectionReference) {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.collectionReference = firebaseFirestore.collection("applications");
    }

    public interface FirebaseDatabaseRepositoryCallBack<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }
}
