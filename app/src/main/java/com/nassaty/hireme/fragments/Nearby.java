package com.nassaty.hireme.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.NearbyMapActivity;
import com.nassaty.hireme.utils.GeoLocator;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nearby extends Fragment {

//    CollectionReference ref = FirebaseFirestore.getInstance().collection("path/to/geofire");
//    GeoFire geoFire = new GeoFire(ref);
    Button viewMap;
    RecyclerView nearby_list;
    UserUtils userUtils;
    GeoLocator geoLocator;


    public Nearby() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);
        userUtils = new UserUtils(getContext());
        viewMap = v.findViewById(R.id.view_mapbtn);
        geoLocator = new GeoLocator(getContext());


        viewMap.setOnClickListener(v1 -> getContext().startActivity(new Intent(getContext(), NearbyMapActivity.class)));
        updateUI(v);

        return v;
    }

    public void updateUI(View v){
        nearby_list = v.findViewById(R.id.near_list);
        nearby_list.setLayoutManager(new LinearLayoutManager(getContext()));
        nearby_list.setHasFixedSize(true);

        geoLocator.getLocations(new GeoLocator.getLocationList() {
            @Override
            public void locations(List<String> uids) {
                if (uids != null){
                    for (String uid : uids){
                        Toast.makeText(getContext(), uid, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "nothing found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
