package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nassaty.hireme.R;
import com.nassaty.hireme.utils.Constants;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatVHolder> {

	List<String> cats;
	Context context;
	onFilterClickListener onFilterClickListener;

	public CatAdapter(Context context, onFilterClickListener listener) {
		this.cats = Constants.getCats();
		this.context = context;
		this.onFilterClickListener = listener;
	}

	@NonNull
	@Override
	public CatVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cat_item, parent, false);
		return new CatVHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final CatVHolder holder, int position) {
		final String title = cats.get(position);
		holder.cat_text.setText(title);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onFilterClickListener.onFilterClick(title);
			}
		});
	}

	@Override
	public int getItemCount() {
		return cats.size();
	}

	class CatVHolder extends RecyclerView.ViewHolder {

		TextView cat_text;

		public CatVHolder(View itemView) {
			super(itemView);
			cat_text = itemView.findViewById(R.id.cat_title);
		}
	}

	public interface onFilterClickListener{
		void onFilterClick(String text);
	}
}
