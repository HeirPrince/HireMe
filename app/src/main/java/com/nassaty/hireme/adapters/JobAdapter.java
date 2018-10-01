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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.JobDetails;
import com.nassaty.hireme.activities.MainActivity;
import com.nassaty.hireme.listeners.NotificationAddedCallBack;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.viewmodels.NewApplicationVModel;
import com.nassaty.hireme.viewmodels.UserVModel;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private AuthUtils authUtils;
    private List<Job> jobs;
    private NewApplicationVModel applicationVModel;
    private UserVModel userVModel;

    public JobAdapter(Context ctx, List<Job> jobs) {
        this.context = ctx;
        this.jobs = jobs;
        this.applicationVModel = ViewModelProviders.of((FragmentActivity) context).get(NewApplicationVModel.class);
        this.authUtils = new AuthUtils(context);
        this.userVModel = new UserVModel();
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
                holder.loadImage(user.getImageTitle(), user.getUID());
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
//                holder.triggerJobStatus(true);
                holder.sendApplication(job.getId());
                if (context instanceof MainActivity){
                    ((NotificationAddedCallBack)context).onAdded();
                }
            }
        });
    }

    // FIXME: 8/23/2018 int java.util.List.size() error returned because list is empty
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


    public class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String ref;
        private Boolean isMine;

        private TextView job_title, owner_names, location, date;
        private CircleImageView owner_image;
        private Button app;


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

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_application:
                    //add app
                    break;
            }
        }

        public void loadImage(String imageTitle, String uid) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(Constants.getImageFolder()).child(uid);

            Glide.with(context)
                    .load(ref.child(imageTitle))
                    .into(owner_image);
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
