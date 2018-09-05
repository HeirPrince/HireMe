package com.nassaty.hireme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.Notif;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// FIXME: 8/11/2018
public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder> {

    List<Notif> notifs;
    Context context;

    public NotifAdapter(List<Notif> notifs, Context context) {
        this.notifs = notifs;
        this.context = context;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notif_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        Notif notif = notifs.get(position);

    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }

    class NotifViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView notif_icon, sender_image;
        public TextView sender_name, content;
        public View delete_notif;

        public NotifViewHolder(View itemView) {
            super(itemView);
            notif_icon = itemView.findViewById(R.id.notif_icon);
            sender_image = itemView.findViewById(R.id.sender_image);
            sender_name = itemView.findViewById(R.id.sender_name);
            content = itemView.findViewById(R.id.content);
            delete_notif = itemView.findViewById(R.id.delete_notif);
        }

        public void setNotification(Notif notification){
            content.setText(notification.getMessage());

        }
    }

}
