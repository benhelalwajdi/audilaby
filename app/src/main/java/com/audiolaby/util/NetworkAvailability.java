package com.audiolaby.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest.Builder;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;


import com.audiolaby.otto.events.ConnectionChangedEvent;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.UiThread;

@EBean(scope = Scope.Singleton)
public class NetworkAvailability {

    private BroadcastReceiver connectivityChangeReceiver;
    private ConnectivityManager connectivityManager;
    private NetworkCallback networkCallback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class networkCallback extends NetworkCallback {
        final /* synthetic */ Context val$context;

        networkCallback(Context context) {
            this.val$context = context;
        }

        public void onAvailable(Network network) {
            NetworkAvailability.this.postConnectionChangedEvent(new ConnectionChangedEvent(ConnectivityUtils.isConnected(this.val$context) ? ConnectionChangedEvent.Status.CONNECTED : ConnectionChangedEvent.Status.DISCONNECTED));
        }

        public void onLost(Network network) {
            NetworkAvailability.this.postConnectionChangedEvent(new ConnectionChangedEvent(ConnectivityUtils.isConnected(this.val$context) ? ConnectionChangedEvent.Status.CONNECTED : ConnectionChangedEvent.Status.DISCONNECTED));
        }
    }

    class broadcastReceiver extends BroadcastReceiver {
        broadcastReceiver() {
        }

        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            if (intent.getBooleanExtra("noConnectivity", false)) {
                NetworkAvailability.this.postConnectionChangedEvent(new ConnectionChangedEvent(ConnectivityUtils.isConnected(context) ? ConnectionChangedEvent.Status.CONNECTED : ConnectionChangedEvent.Status.DISCONNECTED));
            } else {
                NetworkAvailability.this.postConnectionChangedEvent(new ConnectionChangedEvent(ConnectivityUtils.isConnected(context) ? ConnectionChangedEvent.Status.CONNECTED : ConnectionChangedEvent.Status.DISCONNECTED));
            }
        }
    }

    public NetworkAvailability() {
        this.connectivityChangeReceiver = new broadcastReceiver();
    }

    @UiThread
    public void postConnectionChangedEvent(ConnectionChangedEvent event) {
       // this.bus.audioArticle(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void registerNetworkAvailability(Context context) {
        if (VERSION.SDK_INT < 21) {
            context.registerReceiver(this.connectivityChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            return;
        }
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        Builder builder = new Builder();
        this.networkCallback = new networkCallback(context);
        this.connectivityManager.registerNetworkCallback(builder.build(), this.networkCallback);
    }

    public void unregisterNetworkAvailability(Context context, BroadcastReceiver networkAvailabilityReceiver) {
        if (VERSION.SDK_INT < 21) {
            context.unregisterReceiver(this.connectivityChangeReceiver);
        } else {
            this.connectivityManager.unregisterNetworkCallback(this.networkCallback);
        }
        context.unregisterReceiver(networkAvailabilityReceiver);
    }
}
