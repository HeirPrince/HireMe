package com.nassaty.hireme.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.NearbyMapActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby extends Fragment {

//    CollectionReference ref = FirebaseFirestore.getInstance().collection("path/to/geofire");
//    GeoFire geoFire = new GeoFire(ref);
    Button viewMap;

    public Nearby() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        viewMap = v.findViewById(R.id.view_mapbtn);

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), NearbyMapActivity.class));
            }
        });

        return v;
    }
}
