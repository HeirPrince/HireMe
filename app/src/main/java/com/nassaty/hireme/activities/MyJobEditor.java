package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.My_Reviews_adapter;
import com.nassaty.hireme.adapters.SmallAppAdapter;
import com.nassaty.hireme.model.Application;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.Review;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.ReviewUtils;

import java.util.List;

public class MyJobEditor extends AppCompatActivity {

    RecyclerView review_list, app_list;
    TextView job_title, job_description, app_count;
    String job_ref;

    JobUtils jobUtils;
    ReviewUtils reviewUtils;
    ApplicationUtils applicationUtils;

    My_Reviews_adapter adapter;
    SmallAppAdapter smallAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_job_editor);

        review_list = findViewById(R.id.review_list);
        app_list = findViewById(R.id.app_list);
        job_title = findViewById(R.id.job_title);
        job_description = findViewById(R.id.job_description);
        app_count = findViewById(R.id.app_count);
        job_ref = getIntent().getStringExtra("ref_id");

        jobUtils = new JobUtils();
        reviewUtils = new ReviewUtils(this);
        applicationUtils = new ApplicationUtils(this);

        populate(job_ref);
    }

    public void populate(final String job_ref){

        loadReviews(job_ref);
        loadApplications(job_ref);

        jobUtils.getJobById(job_ref, new JobUtils.onJobFoundListener() {
            @Override
            public void foundJob(Job job) {
                job_title.setText(job.getTitle());
                job_description.setText(job.getDescription());
            }
        });
    }

    private void loadApplications(String job_ref) {
        app_list.setLayoutManager(new LinearLayoutManager(this));
        app_list.setHasFixedSize(true);
        applicationUtils.getAppsByJobId(job_ref, new ApplicationUtils.onAppFoundListener() {
           @Override
           public void foundApp(List<Application> applications) {
               smallAppAdapter = new SmallAppAdapter(applications, MyJobEditor.this);
               app_list.setAdapter(smallAppAdapter);

               if (applications.size() > 1){
                   app_count.setText(String.valueOf(applications.size())+ " Applicants");
               }else {
                   app_count.setText(String.valueOf(applications.size())+ " Applicant");
               }

           }
        });
    }

    private void loadReviews(String job_ref) {
        review_list.setLayoutManager(new LinearLayoutManager(this));
        review_list.setHasFixedSize(true);

        reviewUtils.loadReviews(job_ref, new ReviewUtils.getReviewList() {
            @Override
            public void reviews(List<Review> reviews) {
                adapter = new My_Reviews_adapter(reviews, MyJobEditor.this);
                review_list.setAdapter(adapter);
            }
        });
    }
}
