package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.NotifAdapter;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.utils.EmptyRecyclerView;
import com.nassaty.hireme.utils.NotificationUtils;

import java.util.List;

// FIXME: 9/18/2018 I need to migrate to live data
public class Notifications extends AppCompatActivity {

    EmptyRecyclerView notif_list;
    NotifAdapter adapter;
    NotificationUtils notificationUtils;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notificationUtils = new NotificationUtils(this);

        progressBar = findViewById(R.id.loading);
        notif_list = findViewById(R.id.notif_list);


        populate();
    }

    private void populate() {

        final View emptyView = findViewById(R.id.plc_note);
        final View loadingView = findViewById(R.id.notif_list);

        //set up list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        notif_list.setLayoutManager(new LinearLayoutManager(this));
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
                            adapter = new NotifAdapter(notifs, Notifications.this, Notifications.this);
                            notif_list.setAdapter(adapter);
//                            notif_list.setEmptyView(emptyView);
                            notif_list.setLoadingView(loadingView);
                        }
                    });
                }else if (notifs == null){ // FIXME: 10/5/2018 ADD A PLACE HOLDER {@Link R.layout.layout_placeholder_notifications}
                    Toast.makeText(Notifications.this, "no notifs found", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Notifications.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
