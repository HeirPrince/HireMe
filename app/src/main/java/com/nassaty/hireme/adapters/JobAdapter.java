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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.Apply;
import com.nassaty.hireme.activities.JobDetails;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.room.NewFavVModel;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.viewmodels.NewApplicationVModel;
import com.nassaty.hireme.viewmodels.UserVModel;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> implements Filterable {

    private Context context;
    private List<Job> jobs;
    private List<Job> filteredJobs;
    private NewApplicationVModel applicationVModel;
    private UserVModel userVModel;
    private NewFavVModel newFavVModel;
    private StorageUtils storageUtils;

    public JobAdapter(Context ctx, List<Job> jobs) {
        this.context = ctx;
        this.jobs = jobs;
        this.applicationVModel = ViewModelProviders.of((FragmentActivity) context).get(NewApplicationVModel.class);
        this.userVModel = new UserVModel();
        this.storageUtils = new StorageUtils(context);
        this.newFavVModel = ViewModelProviders.of((FragmentActivity) context).get(NewFavVModel.class);
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item_normal, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JobViewHolder holder, int position) {
        final Job job = jobs.get(position);

        holder.job_title.setText(job.getTitle());

        userVModel.getUserByUid(job.getOwner(), new UserVModel.userByUid() {
            @Override
            public void user(User user) {
                holder.owner_names.setText(user.getUser_name());
                storageUtils.downloadUserImage(context, holder.owner_image, user.getUID(), user.getImageTitle());
                holder.location.setText("Kigali");// FIXME: 8/13/2018 set location
                holder.date.setText("Aug, 13");// FIXME: 8/13/2018 set date plz nigga
            }
        });

        holder.setRef(job.getId());
        holder.itemView.setTag(job);

//
//        if (job.getOwner().equals(authUtils.getCurrentUser().getUid())){
//            holder.setMine(true);
//        }else {
//            holder.setMine(false);
//        }
//
//        holder.edit_job.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, EditJob.class);
//                i.putExtra("job_title", job.getTitle());
//                i.putExtra("ref", holder.getRef());
//                context.startActivity(i);
//            }
//        });
//
        PushDownAnim.setPushDownAnimTo(holder.app)
                .setScale( PushDownAnim.MODE_STATIC_DP , 2 )
                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
                .setInterpolatorPush( PushDownAnim.DEFAULT_INTERPOLATOR )
                .setInterpolatorRelease( PushDownAnim.DEFAULT_INTERPOLATOR );

        holder.app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Apply.class);
                intent.putExtra("ref_id", job.getId());
                context.startActivity(intent);
            }
        });

        holder.add2fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFavVModel.addFavorites(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (jobs != null)
            return jobs.size();
        else
            return 0;
    }

    public void addJobs(List<Job> jobList){
        this.jobs = jobList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    filteredJobs = jobs;
                }else {
                    List<Job> filteredList = new ArrayList<>();
                    for (Job job : filteredList){
                        if (job.getTitle().toLowerCase().contains(charString.toLowerCase())){
                              filteredList.add(job);
                        }
                    }
                    filteredJobs = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredJobs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredJobs = (List<Job>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class JobViewHolder extends RecyclerView.ViewHolder {

        private String ref;
        private Boolean isMine;

        private TextView job_title, owner_names, location, date;
        private CircleImageView owner_image;
        private Button app;
        private View add2fav;


        public JobViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, JobDetails.class);
                    i.putExtra("ref_id", ref);
                    context.startActivity(i);
                }
            });

            job_title = itemView.findViewById(R.id.job_title);
            owner_names = itemView.findViewById(R.id.owner);
            location = itemView.findViewById(R.id.location);
            owner_image = itemView.findViewById(R.id.owner_image);
            date = itemView.findViewById(R.id.date_time);
            app = itemView.findViewById(R.id.apply);
            add2fav = itemView.findViewById(R.id.add2fav);
        }

//        private void changeStatus(boolean sent, boolean accepted, boolean rejected) {
//            if (sent) {
//                jobStatus.setText(context.getString(R.string.application_sent));
//                jobStatus.setTextColor(Color.parseColor("#00C853"));
//            } else if (accepted) {
//                jobStatus.setText(context.getString(R.string.application_accepted));
//                jobStatus.setTextColor(Color.parseColor("#00C853"));
//            } else if (rejected) {
//                jobStatus.setText(context.getString(R.string.application_rejected));
//                jobStatus.setTextColor(Color.parseColor("#f44336"));
//            } else {
//
//            }
//        }
//
//        private void triggerJobStatus(boolean b) {
//            if (b) {
//                add_application.setVisibility(View.GONE);
//                jobStatus.setVisibility(View.VISIBLE);
//            } else {
//                add_application.setVisibility(View.VISIBLE);
//                jobStatus.setVisibility(View.GONE);
//            }
//        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getRef() {
            return ref;
        }


//        public void setMine(Boolean mine) {
//            if (mine){
//                edit_job.setVisibility(View.VISIBLE);
//                add_application.setVisibility(View.GONE);
//            }else {
//                edit_job.setVisibility(View.GONE);
//                add_application.setVisibility(View.VISIBLE);
//            }
//        }
    }

}
