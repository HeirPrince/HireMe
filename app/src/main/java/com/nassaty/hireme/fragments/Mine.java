package com.nassaty.hireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.loicteillard.easytabs.EasyTabs;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.MyFragmentAdapter;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mine extends Fragment {

    TextView name, email_phone, jobs_count, apps_count;
    CircleImageView profile_image;
    Button signOut, manageAccount;
    RatingBar ratingBar;
    StorageUtils storageUtils;
    AuthUtils authUtils;
    UserUtils userUtils;
    FirebaseStorage storage;
    JobUtils jobUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_mine, container, false);
        EasyTabs easyTabs = v.findViewById(R.id.easytabs);
        ViewPager viewpager = v.findViewById(R.id.viewpager);
        storageUtils = new StorageUtils(getContext());

        MyFragmentAdapter pagerAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        easyTabs.setViewPager(viewpager, 0);

        name = v.findViewById(R.id.username);
        email_phone = v.findViewById(R.id.email_phone);
        profile_image = v.findViewById(R.id.image);
//        jobs_count = v.findViewById(R.id.job_count);
//        apps_count = v.findViewById(R.id.app_count);
        ratingBar = v.findViewById(R.id.ratingBar);
        manageAccount = v.findViewById(R.id.manage);
        signOut = v.findViewById(R.id.signOut);

        userUtils = new UserUtils(getContext());
        jobUtils = new JobUtils();
        authUtils = new AuthUtils(getContext());
        storage = FirebaseStorage.getInstance();

        setProfileData();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authUtils.doSignOut(getContext());
            }
        });

        return v;
    }

    public void setProfileData(){
         userUtils.getUserByUID(authUtils.getCurrentUser().getUid(), new UserUtils.foundUser() {
             @Override
             public void user(User user) {
                 if (user != null){
                     name.setText(user.getUser_name());
                     storageUtils.downloadUserImage(getContext(), profile_image, user.getUID(), user.getImageTitle());
                     toggleProvider(user);
                     userUtils.getAverageRating(user.getUID(), new UserUtils.ratingListener() {
                         @Override
                         public void rating(float val) {
                             ratingBar.setRating(val);
                         }
                     });

                     jobUtils.getJobCount(user.getUID(), new JobUtils.statsListener() {
                         @Override
                         public void jobs(int val) {
                             jobs_count.setText(String.valueOf(val));
                         }
                     });
                 }
             }
         });
    }

    public void toggleProvider(User user){
        if (user.getEmail() != null){
	        if (user.getEmail().equals("")){
		        email_phone.setText(user.getPhone_number());
	        }else {
		        email_phone.setText(user.getEmail());
	        }
        }else {
        	email_phone.setText(user.getPhone_number());
        }
    }


    public void setStats(){
         userUtils.getUserRating(authUtils.getCurrentUser().getUid(), new UserUtils.userStatsListener() {
             @Override
             public void stats(int rating, int job_count, int app_count) {
//                 if (rating != -1 && job_count != -1 && app_count != -1){
//                     String jbs = String.valueOf(job_count);
//                     String aps = String.valueOf(app_count);
//
//                     jobs_count.setText(jbs);
//                     apps_count.setText(aps);
//                     ratingBar.setRating(rating);
//                 }
             }
         });
    }
}
