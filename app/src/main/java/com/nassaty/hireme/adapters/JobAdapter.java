package com.nassaty.hireme.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.AppList;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.room.NewFavVModel;
import com.nassaty.hireme.viewmodels.NewApplicationVModel;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobs;
    private List<String> ids;
    private NewFavVModel newFavVModel;
    private NewApplicationVModel applicationVModel;

    public JobAdapter(Context ctx, List<Job> jobs, List<String> ids) {
        this.context = ctx;
        this.jobs = jobs;
        this.ids = ids;
        this.newFavVModel = ViewModelProviders.of((FragmentActivity) context).get(NewFavVModel.class);
        this.applicationVModel = ViewModelProviders.of((FragmentActivity) context).get(NewApplicationVModel.class);
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JobViewHolder holder, int position) {
        final Job job = jobs.get(position);
        final String ref = ids.get(position);

        holder.textTitle.setText(job.getTitle());
        holder.textDesc.setText(job.getDescription());
        holder.textSalary.setText(String.valueOf(job.getSalary()));

        holder.setRef(ref);

        holder.itemView.setTag(job);

        holder.add_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.triggerJobStatus(true);
                holder.sendApplication(ref);
            }
        });



        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoFavorites(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void addJobs(List<Job> jobList){
        this.jobs = jobList;
        notifyDataSetChanged();
    }

    public void addtoFavorites(Job job){
        if (job == null){
            Toast.makeText(context, "no job to select", Toast.LENGTH_SHORT).show();
        }else {
            newFavVModel.addFavorites(job);
        }
    }

    public class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textTitle, textDesc, textSalary, jobStatus;
        private Button add_application;
        private View fav;
        private String ref;


        public JobViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textSalary = itemView.findViewById(R.id.textSalary);
            jobStatus = itemView.findViewById(R.id.job_status);
            fav = itemView.findViewById(R.id.fav);
            add_application = itemView.findViewById(R.id.add_application);

            triggerJobStatus(false);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AppList.class);
                    i.putExtra("ref_id", ref);
                    context.startActivity(i);
                }
            });
        }

        private void changeStatus(boolean sent, boolean accepted, boolean rejected) {
            if (sent) {
                jobStatus.setText(context.getString(R.string.app_sent));
                jobStatus.setTextColor(Color.parseColor("#00C853"));
            } else if (accepted) {
                jobStatus.setText(context.getString(R.string.app_accepted));
                jobStatus.setTextColor(Color.parseColor("#00C853"));
            } else if (rejected) {
                jobStatus.setText(context.getString(R.string.app_rejected));
                jobStatus.setTextColor(Color.parseColor("#f44336"));
            } else {
                triggerJobStatus(false);
            }
        }

        private void triggerJobStatus(boolean b) {
            if (b) {
                add_application.setVisibility(View.GONE);
                jobStatus.setVisibility(View.VISIBLE);
            } else {
                add_application.setVisibility(View.VISIBLE);
                jobStatus.setVisibility(View.GONE);
            }
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getRef() {
            return ref;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_application:
                    break;
            }
        }

        private void sendApplication(String ref_id) {
            applicationVModel.sendApplication(ref_id, new applicationAddedListener() {
                @Override
                public void applicationAdded(Boolean state) {
                    if (state) {
                        Toast.makeText(context, "application added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "application not added or may have been cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
