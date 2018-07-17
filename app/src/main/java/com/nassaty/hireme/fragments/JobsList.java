package com.nassaty.hireme.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.AddNewJob;
import com.nassaty.hireme.adapters.JobAdapter;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsList extends Fragment implements OnLongClickListener, View.OnClickListener {

    RecyclerView job_list;
    FloatingActionButton addNew;
    AuthUtils authUtils;
    JobAdapter adapter;
    jobListViewModel viewModel;
    ProgressBar progress;
    Boolean isLoading;
    List<Job> jobList;

    public JobsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_jobs_list, container, false);

        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(jobListViewModel.class);

        addNew = v.findViewById(R.id.addNew);
        progress = v.findViewById(R.id.progress);
        job_list = v.findViewById(R.id.job_list);
        job_list.setLayoutManager(new LinearLayoutManager(getContext()));


        authUtils = new AuthUtils(getContext());
        addNew.setOnClickListener(this);

        getData();

        return v;
    }

    public void getData() {
        setLoading(true);
        jobList = new ArrayList<>();
        viewModel.getJobList(new jobListListener() {
            @Override
            public void jobList(List<Job> jobs) {
                if (jobs != null) {
                    setLoading(false);
                    jobList.clear();
                    int i = 0;
                    for (Job job : jobs){
                        jobList.add(job);
                        i++;
                    }
                    Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                } else {
                    setLoading(false);
                    Toast.makeText(getContext(), "You have no jobs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (jobList == null){
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }else{
            adapter = new JobAdapter(getContext(), jobList);
            job_list.setAdapter(adapter);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.addNew:
                startActivity(new Intent(getContext(), AddNewJob.class));
                break;
        }
    }

    public void setLoading(Boolean loading) {
        if (loading)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }

}
