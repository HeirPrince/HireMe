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
import com.nassaty.hireme.activities.Apply;
import com.nassaty.hireme.activities.JobDetails;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.room.FavListViewModel;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.viewmodels.NewApplicationVModel;
import com.nassaty.hireme.viewmodels.UserVModel;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// FIXME: 10/5/2018 REMOVE FAV BTN
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobs;
    private View.OnLongClickListener onLongClickListener;
    private NewApplicationVModel applicationVModel;
    private UserVModel userVModel;
    private StorageUtils storageUtils;
    private FavListViewModel favListViewModel;

    public FavoritesAdapter(Context ctx, List<Job> jobs, View.OnLongClickListener onLongClickListener) {
        this.context = ctx;
        this.jobs = jobs;
        this.onLongClickListener = onLongClickListener;
        this.applicationVModel = ViewModelProviders.of((FragmentActivity) context).get(NewApplicationVModel.class);
        this.userVModel = new UserVModel();
        this.storageUtils = new StorageUtils(context);
        this.favListViewModel = ViewModelProviders.of((FragmentActivity) context).get(FavListViewModel.class);
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item_normal_fav, parent, false);
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
                context.startActivity(new Intent(context, Apply.class));
            }
        });

        holder.deleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favListViewModel.deleteFav(job);
                notifyDataSetChanged();
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

    public class JobViewHolder extends RecyclerView.ViewHolder{

        private String ref;
        private Boolean isMine;

        private TextView job_title, owner_names, location, date;
        private CircleImageView owner_image;
        private Button app;
        View deleteFav;

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
            deleteFav = itemView.findViewById(R.id.deleteFav);
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getRef() {
            return ref;
        }
    }

}
