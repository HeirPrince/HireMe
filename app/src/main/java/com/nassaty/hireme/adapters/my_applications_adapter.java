package com.nassaty.hireme.adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class my_applications_adapter extends RecyclerView.Adapter<my_applications_adapter.ViewHolder> {

    List<Application> applications;
    jobListViewModel applistVModel;
    UserUtils userUtils;
    JobUtils jobUtils;
    StorageUtils storageUtils;

    private Context context;

    public my_applications_adapter(Context context, List<Application> applications) {
        this.context = context;
        this.applications = applications;
        this.userUtils = new UserUtils();
        this.jobUtils = new JobUtils();
        this.storageUtils = new StorageUtils(context);

        this.applistVModel = ViewModelProviders.of((FragmentActivity) context).get(jobListViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item_mine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Application application = applications.get(position);
        jobUtils.getJobById(application.getJob_id(), new JobUtils.onJobFoundListener() {
            @Override
            public void foundJob(Job job) {
                if (job != null){
                    holder.job_title.setText(job.getTitle());

                    userUtils.getUserByUID(job.getOwner(), new UserUtils.foundUser() {
                        @Override
                        public void user(User user) {
                            if (user != null){
                                holder.user_name.setText(user.getUser_name());
                                holder.loadImage(user.getUID(), user.getImageTitle());
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return applications.size();
    }


    public void removeApplication(int pos){
        applications.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, applications.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView app_id, user_name, job_title;
        CircleImageView user_image;
        Button view, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_image = itemView.findViewById(R.id.user_image);
            job_title = itemView.findViewById(R.id.job_title);
        }

        public void loadImage(String uid, String imageTitle) {
            Glide.with(context)
                    .load(storageUtils.getUserProfileImageRef(uid, imageTitle))
                    .into(user_image);
        }
    }
}
