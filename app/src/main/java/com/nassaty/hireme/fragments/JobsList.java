package com.nassaty.hireme.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.nassaty.hireme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nassaty.hireme.activities.AddNewJob;
import com.nassaty.hireme.adapters.JobAdapter;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsList extends Fragment implements OnLongClickListener, View.OnClickListener {

    RecyclerView job_list;
    FloatingActionButton addNew;
    AuthUtils authUtils;
    FirebaseFirestore firebaseFirestore;
    JobAdapter adapter;

    public JobsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_jobs_list, container, false);

        addNew = v.findViewById(R.id.addNew);
        job_list = v.findViewById(R.id.job_list);
        job_list.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseFirestore = FirebaseFirestore.getInstance();
        authUtils = new AuthUtils(getContext());

        addNew.setOnClickListener(this);

        getData();

        return v;
    }

    public void getData() {

        final List<Job> jobs = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        CollectionReference jobRef = firebaseFirestore
                .collection(Constants.jobRef);

        jobRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.exists()) {
                                Job job = snapshot.toObject(Job.class);
                                String id = snapshot.getId();

                                jobs.add(job);
                                ids.add(id);
                            }
                        }
                        adapter = new JobAdapter(getContext(), jobs, ids);
                        job_list.setAdapter(adapter);
                    }
                });


    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.addNew:
                startActivity(new Intent(getContext(), AddNewJob.class));
                break;
        }
    }
}
