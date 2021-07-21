package com.jpkh.cnpc.activity.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jpkh.cnpc.service.ConnService;

public class OpenServiceBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive", "onReceive: " );
        Intent intent1 = new Intent(context, ConnService.class);
        context.startActivity(intent1);
    }
}
