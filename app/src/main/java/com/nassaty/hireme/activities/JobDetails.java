package com.nassaty.hireme.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.ReviewAdapter;
import com.nassaty.hireme.adapters.SmallAppAdapter;
import com.nassaty.hireme.listeners.SingleJobListener;
import com.nassaty.hireme.listeners.appListListener;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Review;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.ReviewUtils;
import com.nassaty.hireme.viewmodels.AppListVModel;
import com.nassaty.hireme.viewmodels.UserVModel;
import com.nassaty.hireme.viewmodels.jobListViewModel;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobDetails extends AppCompatActivity {
    
    private AuthUtils authUtils;
    private FirebaseStorage firebaseStorage;
    private UserVModel userVModel;
    private AppListVModel appListVModel;
    private ApplicationUtils applicationUtils;
    private ReviewAdapter reviewAdapter;
    private ReviewUtils reviewUtils;
    
    TextView job_title, job_desc, job_location, app_count, rev_count, rev_text;
    CircleImageView user_image, rev_user_image;
    jobListViewModel jobVmodel;
    RecyclerView app_list, rev_list;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        authUtils = new AuthUtils(this);
        firebaseStorage = FirebaseStorage.getInstance();
        applicationUtils = new ApplicationUtils(this);
        reviewUtils = new ReviewUtils(this);

        userVModel = new UserVModel();
        jobVmodel = ViewModelProviders.of(Objects.requireNonNull(this)).get(jobListViewModel.class);
        appListVModel = ViewModelProviders.of(this).get(AppListVModel.class);
        
        job_title = findViewById(R.id.job_title);
        job_desc = findViewById(R.id.job_desc);
        job_location = findViewById(R.id.job_location);
        user_image = findViewById(R.id.img);
        rev_user_image = findViewById(R.id.rev_image);
        app_count = findViewById(R.id.app_count);
        rev_count = findViewById(R.id.count);
        rev_text = findViewById(R.id.review);
        app_list = findViewById(R.id.app_list);
        rev_list = findViewById(R.id.rev_list);

        app_list.setLayoutManager(new LinearLayoutManager(this));
        app_list.setHasFixedSize(true);
        
        getDetails();
    }

    public String getJob_id() {
        return getIntent().getStringExtra("ref_id");
    }

    public void getDetails(){

        jobVmodel.getJobByRef(getJob_id(), new SingleJobListener() {
            @Override
            public void foundJob(final Job job) {
                userVModel.getUserByUid(job.getOwner(), new UserVModel.userByUid() {
                    @Override
                    public void user(User user) {
                        if (user != null){
                            job_title.setText(job.getTitle());
                            job_desc.setText(job.getDescription());

                            loadImage(user.getImageTitle(), user.getUID());

                            appListVModel.getAppsByJobID(job.getId(), new appListListener() {
                                @Override
                                public void appList(List<Application> applications) {
                                    if (applications != null){
                                        populateApplist(job.getId());
                                        app_count.setText(String.valueOf(applications.size()));
                                    }else {
                                        app_count.setText(String.valueOf(0));
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(JobDetails.this, "user not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void populateApplist(String job_id){

        applicationUtils.getAppsByJobId(job_id, new ApplicationUtils.onAppFoundListener() {
            @Override
            public void foundApp(List<Application> applications) {
                Toast.makeText(JobDetails.this, String.valueOf(applications.size()), Toast.LENGTH_SHORT).show();
                SmallAppAdapter appAdapter = new SmallAppAdapter(applications, JobDetails.this);
                app_list.setAdapter(appAdapter);
            }
        });

        loadReviews(job_id);
    }
    
    private void loadImage(String imageTitle, String uid) {
        StorageReference imageRef = firebaseStorage.getReference().child(Constants.getImageFolder()).child(uid);
    
        Glide.with(this)
                .load(imageRef.child(imageTitle))
                .into(user_image);

        Glide.with(this)
                .load(imageRef.child(imageTitle))
                .into(rev_user_image);
    }

    public void loadReviews(String job_id){
        rev_list.setLayoutManager(new LinearLayoutManager(this));
        rev_list.setHasFixedSize(true);
        rev_count.setText(String.valueOf(0));

        reviewUtils.loadReviews(job_id, new ReviewUtils.getReviewList() {
            @Override
            public void reviews(List<Review> reviews) {
                if (reviews == null) {
                    Toast.makeText(JobDetails.this, "no reviews", Toast.LENGTH_SHORT).show();
                }else {
                    rev_count.setText(String.valueOf(reviews.size()));
                    reviewAdapter = new ReviewAdapter(reviews, JobDetails.this);
                    rev_list.setAdapter(reviewAdapter);
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void addReview(View view) {
        Review review = new Review();
        review.setReview(rev_text.getText().toString());
        review.setSender_uid(authUtils.getCurrentUser().getUid());
        review.setJob_id(getJob_id());

        reviewUtils.addReview(review, new ReviewUtils.reviewAddedListener() {
            @Override
            public void isAdded(Boolean isAdded, String message) {
                if (isAdded)
                    Toast.makeText(JobDetails.this, message, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(JobDetails.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
