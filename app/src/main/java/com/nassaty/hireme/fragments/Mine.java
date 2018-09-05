package com.nassaty.hireme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.loicteillard.easytabs.EasyTabs;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.MyFragmentAdapter;
import com.nassaty.hireme.viewmodels.UserVModel;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mine extends Fragment {

    TextView name, email_phone;
    CircleImageView profile_image;

    UserVModel userVModel;
    AuthUtils authUtils;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_mine, container, false);
        EasyTabs easyTabs = v.findViewById(R.id.easytabs);
        ViewPager viewpager = v.findViewById(R.id.viewpager);


        MyFragmentAdapter pagerAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        easyTabs.setViewPager(viewpager, 0);


        name = v.findViewById(R.id.username);
        email_phone = v.findViewById(R.id.email_phone);
        profile_image = v.findViewById(R.id.image);

        userVModel = new UserVModel();
        authUtils = new AuthUtils(getContext());
        storage = FirebaseStorage.getInstance();

        setProfileData();

        return v;
    }

    public void setProfileData(){

        final StorageReference profileRef = storage.getReference().child(Constants.getImageFolder()).child(authUtils.getCurrentUser().getUid());

        userVModel.getUserByUid(authUtils.getCurrentUser().getUid(), new UserVModel.userByUid() {
            @Override
            public void user(User user) {
                if (user != null) {
                    name.setText(user.getUser_name());
                    if (user.getEmail() == null)
                        email_phone.setText(user.getPhone_number());
                    else
                        email_phone.setText(user.getEmail());

                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(profileRef.child(user.getImageTitle()))
                            .into(profile_image);

                }else {
                    Toast.makeText(getContext(), "couldn't get user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
