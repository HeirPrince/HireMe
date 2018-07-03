package com.nassaty.hireme.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionReceiver extends BroadcastReceiver {

    public static ConnectionReceiverListener connectionListener;

    public ConnectionReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet = cm.getActiveNetworkInfo();

        boolean isConnected = activeNet != null && activeNet.isConnectedOrConnecting();

        if (connectionListener != null)
            connectionListener.onNetworkConnectionChanged(isConnected);
    }

    public static boolean checkNetworkStatus(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected)
            return true;
        else
            return false;

    }




    public interface ConnectionReceiverListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
