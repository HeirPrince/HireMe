package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Review;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Reviews_adapter extends RecyclerView.Adapter<My_Reviews_adapter.My_Review_Holder> {

    private List<Review> reviews;
    private Context context;
    private UserUtils userUtils;
    private StorageUtils storageUtils;
    private JobUtils jobUtils;

    public My_Reviews_adapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        this.userUtils = new UserUtils();
        this.storageUtils = new StorageUtils(context);
        this.jobUtils = new JobUtils();
    }

    @NonNull
    @Override
    public My_Review_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_reply_item, parent, false);
        return new My_Review_Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final My_Review_Holder holder, int position) {
        final Review review = reviews.get(position);

        //review part
        holder.review.setText(review.getReview());

        //sender_part
        userUtils.getUserByUID(review.getSender_uid(), new UserUtils.foundUser() {
            @Override
            public void user(User user) {
                if (user != null){
                    holder.user_name.setText(user.getUser_name());

                    //load image
                    storageUtils.downloadUserImage(context, holder.user_image, user.getUID(), user.getImageTitle());

                }
            }
        });

        //owner_part

        if (review.isReplied()){
            holder.owner_reply.setText(review.getReply());
            holder.btnReply.setVisibility(View.GONE);
        }else {
            holder.btnReply.setVisibility(View.VISIBLE);
        }

        jobUtils.getJobById(review.getJob_id(), new JobUtils.onJobFoundListener() {
            @Override
            public void foundJob(Job job) {
                if (job == null) {
                    // no job associated to this ref
                }else {
                    userUtils.getUserByUID(job.getOwner(), new UserUtils.foundUser() {
                        @Override
                        public void user(User user) {
                            if (user == null) {
                                // job doesn't have owner
                            }else {
                                holder.owner.setText(user.getUser_name());
                            }
                        }
                    });
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class My_Review_Holder extends RecyclerView.ViewHolder{

        TextView user_name, review, reply, time, owner, owner_reply;
        Button btnReply;
        CircleImageView user_image;

        public My_Review_Holder(View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_image = itemView.findViewById(R.id.user_image);
            review = itemView.findViewById(R.id.review);
            reply = itemView.findViewById(R.id.reply);
            time = itemView.findViewById(R.id.time);
            owner = itemView.findViewById(R.id.owner);
            owner_reply = itemView.findViewById(R.id.owner_reply);
            btnReply = itemView.findViewById(R.id.reply_btn);
        }
    }
}
