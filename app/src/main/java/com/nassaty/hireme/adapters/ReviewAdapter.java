package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Review;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewItemHolder>{

    public List<Review> reviews;
    public Context context;
    UserUtils userUtils;
    FirebaseStorage firebaseStorage;
    StorageReference imageRef;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        this.userUtils = new UserUtils(context);
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.imageRef = firebaseStorage.getReference().child(Constants.getImageFolder());
    }

    @NonNull
    @Override
    public ReviewItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_item, parent, false);
        return new ReviewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewItemHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review.setText(review.getReview());

        userUtils.getUserByUID(review.getSender_uid(), new UserUtils.foundUser() {
            @Override
            public void user(User user) {
                if (user != null){
                    //find user image
                    Glide.with(context)
                            .load(imageRef.child(user.getUID()).child(user.getImageTitle()))
                            .into(holder.image);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewItemHolder extends RecyclerView.ViewHolder{

        TextView review, time;
        CircleImageView image;


        public ReviewItemHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            review = itemView.findViewById(R.id.review);
            image = itemView.findViewById(R.id.image);
        }
    }
}
