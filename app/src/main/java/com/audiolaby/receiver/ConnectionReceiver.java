package com.audiolaby.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.audiolaby.otto.events.ConnectionChangedEvent;
import com.audiolaby.util.ConnectivityUtils;
import com.audiolaby.view.player.utils.FinishEvent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.greenrobot.eventbus.EventBus;

@EReceiver
public class ConnectionReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        try {
            EventBus.getDefault().register(this);
            EventBus.getDefault().postSticky(new ConnectionChangedEvent(ConnectivityUtils.isConnected(context) ? ConnectionChangedEvent.Status.CONNECTED : ConnectionChangedEvent.Status.DISCONNECTED));
        } catch (Exception e) {
            Log.i("",""+e.toString());
        }
    }
}
