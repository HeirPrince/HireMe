package com.nassaty.hireme.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.utils.ApplicationUtils;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.StorageUtils;
import com.nassaty.hireme.utils.UserUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeVHolder> {

	Context context;
	List<String> employees;
	StorageUtils storageUtils;
	JobUtils jobUtils;
	UserUtils userUtils;
	ApplicationUtils applicationUtils;

	public EmployeeAdapter(Context context, List<String> employees) {
		this.context = context;
		this.employees = employees;
		this.storageUtils = new StorageUtils(context);
		this.jobUtils = new JobUtils();
		this.userUtils = new UserUtils(context);
		this.applicationUtils = new ApplicationUtils(context);
	}

	@NonNull
	@Override
	public EmployeeVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee_item, parent, false);
		return new EmployeeVHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final EmployeeVHolder holder, int position) {
		String uid = employees.get(position);

		userUtils.getUserByUID(uid, user -> {
			if (user != null){
				holder.user_name.setText(user.getUser_name());
				holder.user_skill.setText("Skill");
				storageUtils.downloadUserImage(context, holder.user_image, user.getUID(), user.getImageTitle());

				//load job title
				jobUtils.getJobsByUID(user.getUID(), jobs -> {
					if (jobs != null){
						for (Job job : jobs){
							holder.job_title.setText(job.getTitle());
						}
					}
				});
			}else {
				Toast.makeText(context, "employees not found", Toast.LENGTH_SHORT).show();
				return;
			}
		});




		holder.done.setOnClickListener(v -> {
			final Dialog confirmDialog = new Dialog(context);
			confirmDialog.setContentView(R.layout.dialog_applicant_confirm);

			Button dialog_cancel = confirmDialog.findViewById(R.id.cancel);
			Button dialog_rate = confirmDialog.findViewById(R.id.view_list);
			RatingBar ratingBar = confirmDialog.findViewById(R.id.ratingBar);

			dialog_cancel.setOnClickListener(v1 -> confirmDialog.dismiss());

			dialog_rate.setOnClickListener(v12 -> {
				confirmDialog.dismiss();
				// rate applicant
				int val = ratingBar.getNumStars();
				userUtils.rateApplicant(uid, val, isUserRated -> {
					if (isUserRated){
						Toast.makeText(context, "Thank you for your stats", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(context, "Applicant couldn't be rated", Toast.LENGTH_SHORT).show();
					}
				});

			});

			confirmDialog.show();
		});

	}

	@Override
	public int getItemCount() {
		if (employees.size() != 0)
			return employees.size();
		else
			return 0;
	}

	class EmployeeVHolder extends RecyclerView.ViewHolder {

		CircleImageView user_image;
		TextView user_name, user_skill, hireTime, job_title;
		Button done;

		public EmployeeVHolder(View itemView) {
			super(itemView);
			user_image = itemView.findViewById(R.id.user_image);
			user_name = itemView.findViewById(R.id.user_name);
			user_skill = itemView.findViewById(R.id.user_skill);
			hireTime = itemView.findViewById(R.id.hire_time);
			job_title = itemView.findViewById(R.id.job_title);
			done = itemView.findViewById(R.id.btn_done);
		}
	}
}
