package com.nassaty.hireme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nassaty.hireme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby extends Fragment {

//    CollectionReference ref = FirebaseFirestore.getInstance().collection("path/to/geofire");
//    GeoFire geoFire = new GeoFire(ref);

    public Nearby() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        return v;
    }

}
