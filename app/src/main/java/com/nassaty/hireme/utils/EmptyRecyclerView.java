package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {
	private View emptyView, loadingView;

	final private AdapterDataObserver observer = new AdapterDataObserver() {
		@Override
		public void onChanged() {
			checkIfEmpty();
			checkIfLoading();
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			checkIfEmpty();
			checkIfLoading();
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			checkIfEmpty();
			checkIfLoading();
		}
	};

	public EmptyRecyclerView(Context context) {
		super(context);
	}

	public EmptyRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmptyRecyclerView(Context context, AttributeSet attrs,
	                         int defStyle) {
		super(context, attrs, defStyle);
	}

	void checkIfEmpty() {
		if (emptyView != null && getAdapter() != null) {
			final boolean emptyViewVisible =
					getAdapter().getItemCount() == 0;
			emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
			setVisibility(emptyViewVisible ? GONE : VISIBLE);
		}
	}

	void checkIfLoading(){
		if (loadingView != null && getAdapter() != null){
			final Boolean isLoading = getAdapter().getItemCount() == 0;
			loadingView.setVisibility(isLoading ? VISIBLE : GONE);
			setVisibility(isLoading ? GONE : VISIBLE);
		}
	}

	@Override
	public void setAdapter(Adapter adapter) {
		final Adapter oldAdapter = getAdapter();
		if (oldAdapter != null) {
			oldAdapter.unregisterAdapterDataObserver(observer);
		}
		super.setAdapter(adapter);
		if (adapter != null) {
			adapter.registerAdapterDataObserver(observer);
		}

		checkIfEmpty();
		checkIfLoading();
	}

	public void setEmptyView(View emptyView) {
		this.emptyView = emptyView;
		checkIfEmpty();
	}

	public void setLoadingView(View loadingView){
		this.loadingView = loadingView;
		checkIfEmpty();
	}

}
