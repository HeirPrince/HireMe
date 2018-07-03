package com.nassaty.hireme.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.my_jobs_adapter;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class myJobs extends Fragment {

    private RecyclerView job_list;
    private my_jobs_adapter adapter;
    jobListViewModel jobListViewModel;
    private AuthUtils authUtils;
    private List<String> jobRefs;
    private List<Job> jobList;
    private ProgressBar progress;

    public myJobs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        authUtils = new AuthUtils(getContext());
        jobRefs = new ArrayList<>();
        jobList = new ArrayList<>();

        jobListViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(com.nassaty.hireme.viewmodels.jobListViewModel.class);

        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        job_list = v.findViewById(R.id.job_list);
        job_list.setLayoutManager(new LinearLayoutManager(getContext()));
        job_list.setHasFixedSize(true);

        getData(getJobList(), getJobRefs());

        return v;
    }

    public void getData(final List<Job> jobList, final List<String> jobRefs) {

        jobListViewModel.getJObsByUID(authUtils.getCurrentUser().getPhoneNumber(), new jobListListener() {
            @Override
            public void jobList(List<Job> jobs) {
                if (jobs != null){
                    jobList.addAll(jobs);
                    progress.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getContext(), "you have no jobs yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void refList(List<String> refs) {
                jobRefs.addAll(refs);
            }
        });

        adapter = new my_jobs_adapter(getContext(), getJobList(), getJobRefs());
        job_list.setAdapter(adapter);
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public List<String> getJobRefs() {
        return jobRefs;
    }
}
