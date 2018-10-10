package com.nassaty.hireme.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.MyJobEditor;
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
        //TODO add location and time

        holder.itemView.setTag(job);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MyJobEditor.class);
                i.putExtra("ref_id", job.getId());
                context.startActivity(i);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public class JobViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle, textLocation, textTime;
        private View delete, edit;


        public JobViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation = itemView.findViewById(R.id.location);
            textTime = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }

//        private void deleteJob() {
//            jListVModel.deleteJob(getRef());
//        }
    }
}
