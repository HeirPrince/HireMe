package com.nassaty.hireme.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.NotifAdapter;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.utils.EmptyRecyclerView;
import com.nassaty.hireme.utils.NotificationUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifs extends Fragment {

	EmptyRecyclerView notif_list;
	NotifAdapter adapter;
	NotificationUtils notificationUtils;
	ProgressBar progressBar;

	public Notifs() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v =  inflater.inflate(R.layout.fragment_notifs, container, false);

		notificationUtils = new NotificationUtils(getContext());

		progressBar = v.findViewById(R.id.loading);
		notif_list = v.findViewById(R.id.notif_list);

		populate(v);

		return v;
	}

	private void populate(View v) {

		//set up list
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

		notif_list.setLayoutManager(new LinearLayoutManager(getContext()));
		notif_list.setHasFixedSize(true);

		DividerItemDecoration itemDecoration = new DividerItemDecoration(notif_list.getContext(), layoutManager.getOrientation());

		notif_list.addItemDecoration(itemDecoration);
		progressBar.setVisibility(View.VISIBLE);

		notificationUtils.loadNotifications(new NotificationUtils.getNotificationList() {
			@Override
			public void notifications(final List<Notif> notifs) {
				if (notifs != null) {
					notificationUtils.setListAsRead(notifs, new NotificationUtils.onJobdone() {
						@Override
						public void done(Boolean isDone) {
							progressBar.setVisibility(View.GONE);
							adapter = new NotifAdapter(notifs, getContext(), getActivity());
							notif_list.setAdapter(adapter);
						}
					});
				}else if (notifs == null){ // FIXME: 10/5/2018 ADD A PLACE HOLDER {@Link R.layout.layout_placeholder_notifications}
					Toast.makeText(getContext(), "no notifs found", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
				}
			}
		});


	}

}
