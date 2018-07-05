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
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.listeners.applicationAddedListener;
import com.nassaty.hireme.listeners.applicationRejectedListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;

public class my_applications_adapter extends RecyclerView.Adapter<my_applications_adapter.ViewHolder> {

    List<Application> applications;
    jobListViewModel applistVModel;
    private Context context;

    public my_applications_adapter(Context context, List<Application> applications) {
        this.context = context;
        this.applications = applications;
        this.applistVModel = ViewModelProviders.of((FragmentActivity) context).get(jobListViewModel.class);
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
        holder.app_id.setText(application.getJob_id());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applistVModel.sendApplication(application, new applicationAddedListener() {
                    @Override
                    public void applicationAdded(Boolean state) {
                        if (state){
                            removeApplication(position);
                            holder.accept.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(context, "app not accepted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applistVModel.rejectApplication(application, new applicationRejectedListener() {
                    @Override
                    public void isRejected(Boolean state) {
                        if (state){
                            removeApplication(position);
                            Toast.makeText(context, "application rejected", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "application not rejected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

        TextView app_id;
        Button accept, reject;

        public ViewHolder(View itemView) {
            super(itemView);
            app_id = itemView.findViewById(R.id.app_id);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }
    }
}
