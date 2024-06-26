package de.neuwirthinformatik.alexander.mtuo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import androidx.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TUOWorker extends Worker {
    private NotificationManager notificationManager;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static StringBuilder out = new StringBuilder();
    private ResultReceiver receiver;
    private TUO tuo;
    public static String tuodir;

    public TUOWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                receiver = MainActivity.getReceiver();

                synchronized(this){
                    this.notify();
                }
            }};
        synchronized (run) {
            MainActivity._this.runOnUiThread(run);
            try {
                run.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();

        String[] param = data.getStringArray("param");
        String op = data.getString("operation");


        tuodir = MobileGlobalData.tuodir();
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;
        //Hacky ugly but works
        //SharedPreferences sp = getSharedPreferences(getPackageName()+"_preferences",Context.MODE_MULTI_PROCESS);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        int id = (int)System.currentTimeMillis();
        Log.d("TUO_PROC_IntentService", "onHandleIntent");

        tuo = new TUO(this.getApplicationContext(),param,op,receiver);
        mNotificationManager =
                (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "tuo_channel")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(op) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent nintent = new Intent(getApplicationContext(), OutActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this.getApplicationContext(), id+2, nintent,MainActivity.intent_flag);
        mBuilder.setContentIntent(pi);

        nintent = new Intent(getApplicationContext(), OutActivity.class);
        nintent.putExtra("stop",android.os.Process.myPid());
        pi = PendingIntent.getActivity(this.getApplicationContext(), id+3, nintent,MainActivity.intent_flag);
        mBuilder.addAction(R.drawable.ic_clear,"Cancel All",pi);
        Notification n= mBuilder.build();
        setForegroundAsync(new ForegroundInfo(id+1,n));
        tuo.run();

        Log.d("TUO_IntentService", "onHandleIntentFinished");
        receiver.send(STATUS_FINISHED,Bundle.EMPTY);

        mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
        mBuilder.mActions.clear();
        nintent = new Intent(getApplicationContext(), OutActivity.class);
        final String result = tuo.out.toString();
        nintent.putExtra("text",result);
        //Log.d("TUO_Out",out);
        pi = PendingIntent.getActivity(this.getApplicationContext(), id, nintent,MainActivity.intent_flag);
        mBuilder.setContentIntent(pi);
        if(sp.getBoolean("notification_sound",false))mBuilder.setSound(Uri.parse(sp.getString("notification_sound_uri","content://settings/system/notification_sound")));
        if(sp.getBoolean("notification_vibrate",false))mBuilder.setVibrate(new long[] { 1000, 1000});
        if(sp.getBoolean("notification_led",false))mBuilder.setLights(Color.RED, 3000, 3000);
        mNotificationManager.notify(id, mBuilder.build());
        //stopSelf();
        return Result.success();
    }
        public void callMain(String[] args) {tuo.callMain(args);};

    public String stringFromJNI(String s) {return tuo.stringFromJNI(s);};
}
