package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.adapters.NotifAdapter;
import com.nassaty.hireme.model.Notif;
import com.nassaty.hireme.utils.NotificationUtils;

import java.util.List;

public class Notifications extends AppCompatActivity {

    RecyclerView notif_list;
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
        notif_list.setLayoutManager(new LinearLayoutManager(this));
        notif_list.setHasFixedSize(true);

        populate();
    }

    private void populate() {
        progressBar.setVisibility(View.VISIBLE);

        notificationUtils.loadNotifications(new NotificationUtils.getNotificationList() {
            @Override
            public void notifications(List<Notif> notifs) {
                if (notifs != null) {
                    adapter = new NotifAdapter(notifs, Notifications.this, Notifications.this);
                    notif_list.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }else
                    Toast.makeText(Notifications.this, "no notifs found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
