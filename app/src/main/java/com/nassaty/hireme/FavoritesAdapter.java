package com.nassaty.hireme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nassaty.hireme.model.Job;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobs;
    private View.OnLongClickListener onLongClickListener;

    public FavoritesAdapter(Context ctx, List<Job> jobs, View.OnLongClickListener onLongClickListener) {
        this.context = ctx;
        this.jobs = jobs;
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        final Job job = jobs.get(position);

        holder.textTitle.setText(job.getTitle());
        holder.textDesc.setText(job.getDescription());
        holder.textSalary.setText(String.valueOf(job.getSalary()));

        holder.itemView.setTag(job);

        holder.itemView.setOnLongClickListener(onLongClickListener);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void addJobs(List<Job> jobList){
        this.jobs = jobList;
        notifyDataSetChanged();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle, textDesc, textSalary;

        public JobViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textSalary = itemView.findViewById(R.id.textSalary);
        }
    }

}
