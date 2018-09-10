package com.nassaty.hireme.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nassaty.hireme.R;
import com.nassaty.hireme.intentActivities.replyReview;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.utils.AuthUtils;

import java.util.List;

// FIXME: 8/11/2018
public class NotifAdapter extends RecyclerView.Adapter {

    private List<Notif> notifs;
    private AuthUtils authUtils;
    private Context context;
    private Activity activity;

    public NotifAdapter(List<Notif> notifs, Context context, Activity activity) {
        this.notifs = notifs;
        this.context = context;
        this.authUtils = new AuthUtils(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType){
            case Notif.REVIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item_review, parent, false);

                return new ReviewTypeHolder(view);

            case Notif.JOB_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item_job, parent, false);
                return new JobTypeHolder(view);

            case Notif.APPLICATION_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item_application, parent, false);
                return new ApplicationTypeHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (notifs.get(position).type){
            case 0:
                return Notif.REVIEW_TYPE;
            case 1:
                return Notif.JOB_TYPE;
            case 2:
                return Notif.APPLICATION_TYPE;
            default:
                return -1;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Notif notification = notifs.get(position);

        if (notification != null){
            switch (notification.type){
                case 0:
                    if (notification.getReceiver_uid().equals(authUtils.getCurrentUser().getUid())){
                        ((ReviewTypeHolder)holder).content.setText(notification.getText());
                        ((ReviewTypeHolder)holder).time.setText(notification.getTime());

                        //reply
                        ((ReviewTypeHolder)holder).reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ((ReviewTypeHolder)holder).replyReview("1", activity);
                                MaterialDialog m = new MaterialDialog(context);
                                m
                                        .message(android.R.string.unknownName, "hello")
                                        .show();
                            }
                        });
                    }

                    break;

                case 1:
                    ((JobTypeHolder)holder).content.setText(notification.getText());
                    ((JobTypeHolder)holder).time.setText(notification.getTime());

                    ((JobTypeHolder)holder).view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "job viewed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

                case 2:

                    if (notification.getReceiver_uid().contains(authUtils.getCurrentUser().getUid())){
                        ((ApplicationTypeHolder)holder).content.setText(notification.getText());
                        ((ApplicationTypeHolder)holder).time.setText(notification.getTime());

                        ((ApplicationTypeHolder)holder).view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "application viewed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }

    class ReviewTypeHolder extends RecyclerView.ViewHolder{

        TextView content, time;
        View reply;


        public ReviewTypeHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.notification_content);
            time = itemView.findViewById(R.id.time);
            reply = itemView.findViewById(R.id.reply);
        }

        public void replyReview(String review_id, Activity activity){
            Intent replyIntent = new Intent(context, replyReview.class);
            activity.startActivityForResult(replyIntent, 100);
        }
    }

    class JobTypeHolder extends RecyclerView.ViewHolder{

        TextView content, time;
        View view;

        public JobTypeHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.notification_content);
            time = itemView.findViewById(R.id.time);
            view = itemView.findViewById(R.id.view_job);
        }
    }

    class ApplicationTypeHolder extends RecyclerView.ViewHolder{

        TextView content, time;
        View view;

        public ApplicationTypeHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.notification_content);
            time = itemView.findViewById(R.id.time);
            view = itemView.findViewById(R.id.view_application);
        }
    }



}
