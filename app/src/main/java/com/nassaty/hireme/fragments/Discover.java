package com.nassaty.hireme.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.SectionAdapter;
import com.nassaty.hireme.listeners.jobListListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Section;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.RecyclerViewType;
import com.nassaty.hireme.utils.UserUtils;
import com.nassaty.hireme.viewmodels.SharedViewModel;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment{

    RecyclerView job_list;
    jobListViewModel viewModel;
    ProgressBar progress;
    AuthUtils authUtils;
    UserUtils userUtils;
    List<Section> sections;
    LinearLayout plc_holder;
    private SharedViewModel sharedViewModel;
    private onScrollList scrollList;

    public Discover() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discover, container, false);

        progress = v.findViewById(R.id.progress);
        job_list = v.findViewById(R.id.job_list);
        plc_holder = v.findViewById(R.id.plc_holder);

        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(jobListViewModel.class);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        authUtils = new AuthUtils(getContext());
        userUtils = new UserUtils(getContext());
        sections = new ArrayList<>();

        job_list.setLayoutManager(new LinearLayoutManager(getContext()));
        job_list.setHasFixedSize(true);
        job_list.setItemAnimator(new DefaultItemAnimator());

        scrollList = (onScrollList) getActivity();

        userUtils.getUserRating(authUtils.getCurrentUser().getUid(), new UserUtils.userStatsListener() {
            @Override
            public void stats(int rating, int job_count, int app_count) {
                
            }
        });

        toggleScroll(job_list);

        getData();

        return v;
    }

    private void toggleScroll(RecyclerView job_list) {
        job_list.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0)
                    scrollList.scrolled(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    scrollList.scrolled(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public void getData() {
        setLoading(true);

        final List<Section> sections = new ArrayList<>();
        final List<Job> my_jobs = new ArrayList<>();
        final List<Job> all_jobs = new ArrayList<>();

        final Section section_others = new Section();
        final Section section_mine = new Section();

        viewModel.getJobList(new jobListListener() {
            @Override
            public void jobList(final List<Job> jobs) {
                if (jobs != null) {

                    //mine
                    refreshLists(my_jobs, sections);
                    for (Job job : jobs) {
                        if (authUtils.isMine(job.getOwner())) {
                            my_jobs.add(job);
                            section_mine.setSectionLabel("Mine");
                            section_mine.setJobs(my_jobs);
                        }
                    }

                    //all
                    all_jobs.clear();
                    for (Job job : jobs) {
                        all_jobs.add(job);
                        section_others.setSectionLabel("All");
                        section_others.setJobs(all_jobs);
                    }


                    sections.add(section_mine);
                    sections.add(section_others);

                    SectionAdapter adapter = new SectionAdapter(getContext(), sections, RecyclerViewType.VERTICAL_ORIENTATION, sharedViewModel);
                    job_list.setAdapter(adapter);

                } else {
                    refreshLists(jobs, sections);
                }
            }

        });
    }

    public void refreshLists(List<Job> jobs, List<Section> sections){
        if (!jobs.isEmpty() && !sections.isEmpty()){
            jobs.clear();
            sections.clear();
            setLoading(false);
            job_list.setVisibility(View.VISIBLE);
        }else {
            setLoading(false);
            job_list.setVisibility(View.GONE);
        }
    }


    public interface onScrollList{
        void scrolled(Boolean state);
    }

    public void setLoading(Boolean loading) {
        if (loading)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }

}
