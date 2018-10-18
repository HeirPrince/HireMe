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

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.EmpUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    List<Application> applications;
    jobListViewModel applistVModel;
    AuthUtils authUtils;
    UserUtils userUtils;
    private Context context;
    StorageUtils storageUtils;
    EmpUtils empUtils;

    public ApplicationAdapter(Context context, List<Application> applications) {
        this.context = context;
        this.applications = applications;
        this.authUtils = new AuthUtils(context);
        this.applistVModel = ViewModelProviders.of((FragmentActivity) context).get(jobListViewModel.class);
        this.userUtils = new UserUtils(context);
        this.storageUtils = new StorageUtils(context);
        this.empUtils = new EmpUtils(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_application_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Application application = applications.get(position);

        holder.setData(application);

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
                    @Override
                    public void user(User employer) {
                        userUtils.getUserByUID(application.getSender(), new UserUtils.foundUser() {
                            @Override
                            public void user(User employee) {
                                empUtils.addEmployee(employer.getUID(), employee.getUID());
                            }
                        });

                    }
                });
            }
        });

//        holder.reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                applistVModel.rejectApplication(application, new applicationRejectedListener() {
//                    @Override
//                    public void isRejected(Boolean state) {
//                        if (state){
//                            removeApplication(position);
//                            Toast.makeText(context, "application rejected", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(context, "application not rejected", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
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
        CircleImageView user_image;
        TextView user_name, user_skill, time;
        Button accept;

        public ViewHolder(View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.accept);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_skill= itemView.findViewById(R.id.user_skill);
            time = itemView.findViewById(R.id.app_time);

        }

        public void setData(Application application){
             userUtils.getUserByUID(application.getSender(), new UserUtils.foundUser() {
                 @Override
                 public void user(User user) {
                     if (user != null){
                         user_name.setText(user.getUser_name());
                         user_skill.setText("Stonner");
                         storageUtils.downloadUserImage(context, user_image, user.getUID(), user.getImageTitle());
                     }
                 }
             });
        }
    }


}
