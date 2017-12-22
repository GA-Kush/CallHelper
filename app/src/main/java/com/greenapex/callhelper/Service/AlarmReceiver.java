package com.greenapex.callhelper.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pc01 on 21/12/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CallHelper","We are in Receiver");
        Toast.makeText(context, "Reminder Set", Toast.LENGTH_SHORT).show();

        Intent service_intent=new Intent(context,RingTonePlayingService.class);
        context.startService(service_intent);
    }
}
