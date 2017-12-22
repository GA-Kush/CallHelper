package com.greenapex.callhelper.Service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.greenapex.callhelper.MainActivity;
import com.greenapex.callhelper.R;

/**
 * Created by pc01 on 21/12/17.
 */

public class RingTonePlayingService extends IntentService {

    MediaPlayer mediaPlayer;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RingTonePlayingService() {
        super(RingTonePlayingService.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        NotificationManager notificationManager=context.getSystemService(NotificationManager.class);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setCategory(Notification.CATEGORY_CALL)
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setContentTitle("Notification");

        notificationManager.notify();

    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        Log.e("CallHelper", "StartCommand");
////        mediaPlayer = MediaPlayer.create(this, R.raw.dove);
////        mediaPlayer.start();
//
//        //for notification
//        NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intent_main_Activity = new Intent(this.getApplicationContext(), MainActivity.class);
//        PendingIntent pendingIntent_main_Activity = PendingIntent.getActivity(this, 0, intent_main_Activity, 0);
//
//        Notification notification_popup = new Notification.Builder(this)
//                .setContentTitle("Reminder Call")
//                .setContentText("Call This Person")
//                .setContentIntent(pendingIntent_main_Activity)
//                .setAutoCancel(true)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .build();
//        notify_manager.notify(startId, notification_popup);
//
//        return START_NOT_STICKY;
//    }
//
//    public void onDestroy() {
//        Toast.makeText(this, "On Destroy Call", Toast.LENGTH_SHORT).show();
//        Log.e("CallHelper", "onDestroy Call");
//    }


}
