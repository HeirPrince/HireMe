package com.nassaty.hireme.firebase;

import android.arch.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class FirebaseQueryLiveData extends LiveData<DocumentSnapshot> {

    private static final String LOG = "FirebaseQueryLiveData";

    private DocumentReference documentReference;
    private MyValueEventListener myValueEventListener = new MyValueEventListener();

    public FirebaseQueryLiveData(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    @Override
    protected void onActive() {
        super.onActive();
        documentReference.addSnapshotListener(myValueEventListener);
    }

    public class MyValueEventListener implements EventListener<DocumentSnapshot>{

        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            if (e == null)
                return;
            setValue(documentSnapshot);
        }
    }
}
