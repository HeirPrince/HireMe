package com.nassaty.hireme.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.AppList;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;

public class my_jobs_adapter extends RecyclerView.Adapter<my_jobs_adapter.JobViewHolder> {

    private Context context;
    private List<Job> jobs;
    private jobListViewModel jListVModel;

    public my_jobs_adapter(Context ctx, List<Job> jobs) {
        this.context = ctx;
        this.jobs = jobs;
        this.jListVModel = ViewModelProviders.of((FragmentActivity) context).get(jobListViewModel.class);
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item_mine, parent, false);
        return new my_jobs_adapter.JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final my_jobs_adapter.JobViewHolder holder, final int position) {
        final Job job = jobs.get(position);

        holder.textTitle.setText(job.getTitle());
        holder.textDesc.setText(job.getDescription());
        holder.textSalary.setText(String.valueOf(job.getSalary()));

        holder.itemView.setTag(job);

        holder.delete_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deleteJob();
                removeJob(position);
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

    public void removeJob(int pos){
        jobs.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, jobs.size());
    }

    public class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textTitle, textDesc, textSalary;
        private Button delete_application;
        private String ref;


        public JobViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textSalary = itemView.findViewById(R.id.textSalary);
            delete_application = itemView.findViewById(R.id.delete_application);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AppList.class);
                    i.putExtra("ref_id", ref);
                    context.startActivity(i);
                }
            });
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

        private void deleteJob() {
            jListVModel.deleteJob(getRef());
        }
    }
}
