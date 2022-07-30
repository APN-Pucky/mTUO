package de.neuwirthinformatik.alexander.mtuo;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;

public class TUOJobIntentService extends JobIntentService {
        public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static StringBuilder out = new StringBuilder();
    private ResultReceiver receiver;
    private TUO tuo;
    public static String tuodir;

    public TUOJobIntentService() {

        Log.d("TUO_JobIntentService", "loading TUO lib");
        //System.loadLibrary("tuo");
        Log.d("TUO_JobIntentService", "load TUO lib");
        //_this = this;
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        onHandleIntent(intent);

    }


    protected void onHandleIntent(Intent intent) {
        tuodir = GlobalData.tuodir();
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;
        //Hacky ugly but works
        //SharedPreferences sp = getSharedPreferences(getPackageName()+"_preferences",Context.MODE_MULTI_PROCESS);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int id = (int)System.currentTimeMillis();
        Log.d("TUO_PROC_IntentService", "onHandleIntent");
        receiver = intent.getParcelableExtra("receiver");
        String[] param = intent.getStringArrayExtra("param");
        String op = intent.getStringExtra("operation");


        tuo = new TUO(this,param,op,receiver);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "tuo_channel")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(op) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent nintent = new Intent(getApplicationContext(), OutActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this, id+2, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);

        nintent = new Intent(getApplicationContext(), OutActivity.class);
        nintent.putExtra("stop",android.os.Process.myPid());
        pi = PendingIntent.getActivity(this, id+3, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.addAction(R.drawable.ic_clear,"Cancel All",pi);
/*
        Intent deleteIntent = new Intent(this, TUOIntentService.class);
        deleteIntent.putExtra("stop", true);
        PendingIntent deletePendingIntent = PendingIntent.getService(this,
                0,
                deleteIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.addAction(R.drawable.ic_cancel,"Cancel",deletePendingIntent);*/

        startForeground(id+1,mBuilder.build());
        tuo.run();


        mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
        mBuilder.mActions.clear();
        nintent = new Intent(getApplicationContext(), OutActivity.class);
        final String result = tuo.out.toString();
        nintent.putExtra("text",result);
        //Log.d("TUO_Out",out);
        pi = PendingIntent.getActivity(this, id, nintent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        if(sp.getBoolean("notification_sound",false))mBuilder.setSound(Uri.parse(sp.getString("notification_sound_uri","content://settings/system/notification_sound")));
        if(sp.getBoolean("notification_vibrate",false))mBuilder.setVibrate(new long[] { 1000, 1000});
        if(sp.getBoolean("notification_led",false))mBuilder.setLights(Color.RED, 3000, 3000);
        stopForeground(true);
        mNotificationManager.notify(id, mBuilder.build());
        //stopSelf();
    }


}