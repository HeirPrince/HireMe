package com.nassaty.hireme.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.activities.Worker_list;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.JobUtils;
import com.nassaty.hireme.utils.StorageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeVHolder> {

	Context context;
	List<User> users;
	StorageUtils storageUtils;
	JobUtils jobUtils;

	public EmployeeAdapter(Context context, List<User> users) {
		this.context = context;
		this.users = users;
		this.storageUtils = new StorageUtils(context);
		this.jobUtils = new JobUtils();
	}

	@NonNull
	@Override
	public EmployeeVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee_item, parent, false);
		return new EmployeeVHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final EmployeeVHolder holder, int position) {
		User user = users.get(position);
		holder.user_name.setText(user.getUser_name());
		holder.user_skill.setText("Skill");
		storageUtils.downloadUserImage(context, holder.user_image, user.getUID(), user.getImageTitle());

		//load job title
		jobUtils.getJobsByUID(user.getUID(), new JobUtils.jobListCallback() {
			@Override
			public void jobs(List<Job> jobs) {
				if (jobs != null){
					for (Job job : jobs){
						holder.job_title.setText(job.getTitle());
					}
				}
			}
		});

		holder.del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog confirmDialog = new Dialog(context);
				confirmDialog.setContentView(R.layout.dialog_applicant_confirm);

				final Button dialog_cancel = confirmDialog.findViewById(R.id.cancel);
				Button dialog_viewList = confirmDialog.findViewById(R.id.view_list);

				dialog_cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						confirmDialog.dismiss();
					}
				});

				dialog_viewList.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						confirmDialog.dismiss();
						Intent intent = new Intent(context, Worker_list.class);
						context.startActivity(intent);
					}
				});

				confirmDialog.show();
			}
		});

	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	class EmployeeVHolder extends RecyclerView.ViewHolder {

		CircleImageView user_image;
		TextView user_name, user_skill, hireTime, job_title;
		Button del;

		public EmployeeVHolder(View itemView) {
			super(itemView);
			user_image = itemView.findViewById(R.id.user_image);
			user_name = itemView.findViewById(R.id.user_name);
			user_skill = itemView.findViewById(R.id.user_skill);
			hireTime = itemView.findViewById(R.id.hire_time);
			job_title = itemView.findViewById(R.id.job_title);
			del = itemView.findViewById(R.id.btn_del);
		}
	}
}
