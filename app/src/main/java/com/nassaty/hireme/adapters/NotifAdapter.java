package com.nassaty.hireme.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.ApplicantDetails;
import com.nassaty.hireme.activities.MainActivity;
import com.nassaty.hireme.listeners.NotificationAddedCallBack;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.NotificationUtils;
import com.nassaty.hireme.utils.ReviewUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// FIXME: 8/11/2018
public class NotifAdapter extends RecyclerView.Adapter {

    private List<Notif> notifs;
    private AuthUtils authUtils;
    private ReviewUtils reviewUtils;
    private UserUtils userUtils;
    private StorageUtils storageUtils;
    private ApplicationUtils applicationUtils;
    private NotificationUtils notificationUtils;
    private Context context;
    private Activity activity;

    public NotifAdapter(List<Notif> notifs, Context context, Activity activity) {
        this.notifs = notifs;
        this.context = context;
        this.authUtils = new AuthUtils(context);
        this.reviewUtils = new ReviewUtils(context);
        this.userUtils = new UserUtils();
        this.storageUtils = new StorageUtils(context);
        this.applicationUtils = new ApplicationUtils(context);
        this.notificationUtils = new NotificationUtils(context);
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

                        ((ReviewTypeHolder)holder).content1.setText(notification.getText());
                        ((ReviewTypeHolder)holder).time1.setText(notification.getTime());

                        //reply
                        ((ReviewTypeHolder)holder).reply_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String reply = ((ReviewTypeHolder)holder).reply_input.getText().toString();
                                ((ReviewTypeHolder)holder).replyReview(notification.getContent_id(), reply);
                                ((ReviewTypeHolder)holder).foldingCell.fold(true);
                            }
                        });

                        //user part
                        userUtils.getUserByUID(notification.getSender_uid(), new UserUtils.foundUser() {
                            @Override
                            public void user(User user) {
                                storageUtils.downloadUserImage(context, ((ReviewTypeHolder)holder).user_image1, user.getUID(), user.getImageTitle());
                                storageUtils.downloadUserImage(context, ((ReviewTypeHolder)holder).user_image2, user.getUID(), user.getImageTitle());
                                ((ReviewTypeHolder)holder).user_name.setText(user.getUser_name());
                                ((ReviewTypeHolder)holder).time2.setText(notification.getTime());
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
                                Intent i = new Intent(context, ApplicantDetails.class);
                                i.putExtra("receiver_uid", notification.getReceiver_uid());
                                i.putExtra("sender_uid", notification.getSender_uid());
                                i.putExtra("obj_ref", notification.getContent_id());
                                context.startActivity(i);
                            }
                        });

                        if (!notification.getId().equals("")){
                            ((ApplicationTypeHolder)holder).delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //delete application


                                    notificationUtils.deleteNotification(notification.getId(), new NotificationUtils.onNotifDeleted() {
                                        @Override
                                        public void deleted(Boolean isDeleted) {
                                            if (isDeleted){
                                                Toast.makeText(context, "yes is deleted", Toast.LENGTH_SHORT).show();
                                                applicationUtils.deleteApplication(notification.getContent_id(), new ApplicationUtils.jobDone() {
                                                    @Override
                                                    public void onJobDone(Boolean done) {
                                                        if (done) {
                                                            Toast.makeText(context, "application deleted", Toast.LENGTH_SHORT).show();                                                   if (context instanceof MainActivity){
                                                                ((NotificationAddedCallBack)context).onRemoved();
                                                            }
                                                            notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(context, "application could not be deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                                Toast.makeText(context, "no its still there", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    
                                }
                            });
                        }else {
                            Toast.makeText(context, "sth wrong", Toast.LENGTH_SHORT).show();
                        }

                        //user part
                        userUtils.getUserByUID(notification.getSender_uid(), new UserUtils.foundUser() {
                            @Override
                            public void user(User user) {
                                storageUtils.downloadUserImage(context, ((ApplicationTypeHolder)holder).user_image, user.getUID(), user.getImageTitle());
                                ((ApplicationTypeHolder)holder).user_name.setText(user.getUser_name());
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

        TextView content1, time1, user_name, content2, time2;
        CircleImageView user_image1, user_image2;
        Button reply_btn;
        EditText reply_input;
        FoldingCell foldingCell;


        public ReviewTypeHolder(View itemView) {
            super(itemView);

            content1 = itemView.findViewById(R.id.notification_content1);
            time1 = itemView.findViewById(R.id.time1);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            user_image1 = itemView.findViewById(R.id.user_image1);
            user_image2 = itemView.findViewById(R.id.user_image2);
            user_name = itemView.findViewById(R.id.user_name);
            time2 = itemView.findViewById(R.id.time2);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            reply_input = itemView.findViewById(R.id.reply_input);

            foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foldingCell.toggle(false);
                }
            });
        }

        public void replyReview(String content_id, String reply){
            reviewUtils.ReplyReview(content_id, reply, new ReviewUtils.reviewReplied() {
                @Override
                public void isReplied(Boolean reply) {
                    if (reply){
                        Toast.makeText(context, "Review replied", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Review not replied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

        TextView content, time, user_name;
        CircleImageView user_image;
        Button view, delete;

        public ApplicationTypeHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.username);
            view = itemView.findViewById(R.id.view);
            delete = itemView.findViewById(R.id.delete);
        }
    }



}
