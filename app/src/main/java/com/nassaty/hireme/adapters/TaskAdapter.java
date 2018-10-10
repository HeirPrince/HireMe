package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskVHolder> {

	Context context;
	List<Task> tasks;

	public TaskAdapter(Context context, List<Task> tasks) {
		this.context = context;
		this.tasks = tasks;
	}

	@NonNull
	@Override
	public TaskVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_item, parent, false);
		return new TaskVHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull TaskVHolder holder, final int position) {
		final Task task = tasks.get(position);
		holder.title.setText(task.getTitle());
		holder.salary.setText(String.valueOf(task.getSalary()));

		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteTAsk(task, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return tasks.size();
	}

	public void deleteTAsk(Task task, int position){
		tasks.remove(task);
		notifyItemRemoved(position);
		notifyDataSetChanged();
	}

	interface TotalListener{
		void total(int total);
	}

	class TaskVHolder extends RecyclerView.ViewHolder {

		TextView title, salary;
		View delete;

		public TaskVHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.title);
			salary = itemView.findViewById(R.id.salary);
			delete = itemView.findViewById(R.id.delete);

		}
	}
}
