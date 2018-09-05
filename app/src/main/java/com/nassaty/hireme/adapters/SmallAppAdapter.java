package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SmallAppAdapter extends RecyclerView.Adapter<SmallAppAdapter.AppViewHolder>{

    public List<Application> applications;
    private Context context;
    JobUtils jobUtils;
    ApplicationUtils applicationUtils;
    AuthUtils authUtils;
    UserUtils userUtils;
    StorageUtils storageUtils;

    public SmallAppAdapter(List<Application> applications, Context context) {
        this.applications = applications;
        this.context = context;
        this.jobUtils = new JobUtils();
        this.applicationUtils = new ApplicationUtils();
        this.authUtils = new AuthUtils(context);
        this.userUtils = new UserUtils();
        this.storageUtils = new StorageUtils(context);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item_readonly, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.setApp(application);
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder{

        TextView names;
        CircleImageView profile_pic;
        RatingBar app_rating;

        public AppViewHolder(View itemView) {
            super(itemView);
            app_rating = itemView.findViewById(R.id.applicant_rating);
            names = itemView.findViewById(R.id.applicant_names);
            profile_pic = itemView.findViewById(R.id.applicant_image);
        }

        public void setApp(final Application application) {
             userUtils.getUserByUID(application.getSender(), new UserUtils.foundUser() {
                 @Override
                 public void user(User user) {
                     if (user != null){
                         names.setText(user.getUser_name());
                         Glide.with(context)
                                 .load(storageUtils.getUserProfileImageRef(application.getSender(), user.getImageTitle()))
                                 .into(profile_pic);
                     }
                 }
             });
        }
    }

}
