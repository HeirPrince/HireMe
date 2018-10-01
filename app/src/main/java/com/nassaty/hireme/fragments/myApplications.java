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
import com.nassaty.hireme.adapters.my_applications_adapter;
import com.nassaty.hireme.listeners.SingleAppListener;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.viewmodels.AppListVModel;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class myApplications extends Fragment {

    AppListVModel viewModel;
    jobListViewModel jobListViewModel;
    private RecyclerView app_list;
    private my_applications_adapter adapter;
    private AuthUtils authUtils;
    private List<Application> applications;
    private ProgressBar progress;
    private ApplicationUtils applicationUtils;
    private JobUtils jobUtils;

    public myApplications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_applications, container, false);
        authUtils = new AuthUtils(getContext());
        applicationUtils = new ApplicationUtils(getContext());
        jobUtils = new JobUtils();

        applications = new ArrayList<>();
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AppListVModel.class);
        jobListViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(jobListViewModel.class);

        app_list = v.findViewById(R.id.app_list);
        app_list.setLayoutManager(new LinearLayoutManager(getContext()));
        app_list.setHasFixedSize(true);

        progress = v.findViewById(R.id.progress);

        getApps();

        return v;
    }

    public void getData() {
        final List<Application> applications = new ArrayList<>();
        progress.setVisibility(View.VISIBLE);

        final String phone = authUtils.getCurrentUser().getPhoneNumber();
        final String uid = authUtils.getCurrentUser().getUid();

        jobListViewModel.getJObsByUID(phone, new jobListListener() {
            @Override
            public void jobList(List<Job> jobs) {
                if (jobs == null) {
                    for (Job job : jobs){
                        if (job.getOwner().equals(phone)){
                            viewModel.getAppByUID(String.valueOf(job.getId()), uid, new SingleAppListener() {
                                @Override
                                public void isFound(Boolean isFound, Application application) {
                                    if (isFound && application != null){
                                        applications.add(application);
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "found jobs associated to this man", Toast.LENGTH_SHORT).show();
                }
            }

        });

        adapter = new my_applications_adapter(getContext(), applications);
        app_list.setAdapter(adapter);
    }

    public void getApps(){
        final String uid = authUtils.getCurrentUser().getUid();
        progress.setVisibility(View.VISIBLE);

        applicationUtils.getAppsByUid(uid, new ApplicationUtils.onAppFoundListener() {
            @Override
            public void foundApp(List<Application> applications) {
                if (applications != null){
                    progress.setVisibility(View.GONE);
                    adapter = new my_applications_adapter(getContext(), applications);
                    app_list.setAdapter(adapter);
                }
                else
                    Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

