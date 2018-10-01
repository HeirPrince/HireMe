package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantDetails extends AppCompatActivity {

	android.support.v7.widget.Toolbar toolbar;
	String applicant_id;

	TextView user_name, user_email, user_phone, skill;
	CircleImageView user_image;

	UserUtils userUtils;
	StorageUtils storageUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicant_details);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Applicant Details");

		applicant_id = getIntent().getStringExtra("sender_uid");

		//utils
		userUtils = new UserUtils();
		storageUtils = new StorageUtils(this);

		//fields
		user_name = findViewById(R.id.user_name);
		user_image = findViewById(R.id.user_image);
		user_email = findViewById(R.id.user_email);
		user_phone = findViewById(R.id.user_phone);
		
		getDetails(applicant_id);

	}

	private void getDetails(String applicant_id) {
		userUtils.getUserByUID(applicant_id, new UserUtils.foundUser() {
			@Override
			public void user(User user) {
				if (user != null){
					user_name.setText(user.getUser_name());
					if (!user.getEmail().equals("")){
						user_email.setText(user.getEmail());
					}else {
						user_email.setText("not set");
					}

//					if (!user.getPhone_number().equals("")){
//						user_phone.setText(user.getPhone_number());
//					}else {
//						user_phone.setText("not set");
//					}
					storageUtils.downloadUserImage(ApplicantDetails.this, user_image, user.getUID(), user.getImageTitle());
				}
			}
		});
	}
}
