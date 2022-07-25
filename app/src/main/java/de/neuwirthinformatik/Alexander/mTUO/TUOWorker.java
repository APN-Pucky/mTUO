package de.neuwirthinformatik.Alexander.mTUO;

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
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;

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


        tuodir = GlobalData.tuodir();
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;
        //Hacky ugly but works
        //SharedPreferences sp = getSharedPreferences(getPackageName()+"_preferences",Context.MODE_MULTI_PROCESS);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        int id = (int)System.currentTimeMillis();
        Log.d("TUO_PROC_IntentService", "onHandleIntent");

        tuo = new TUO(this.getApplicationContext(),receiver);
        out.setLength(0);
        out.append("./");
        out.append(param[0] + " ");
        out.append("\"");
        out.append(param[1] + "\" ");
        out.append("\"");
        out.append(param[2] + "\" ");
        for (int i = 3; i < param.length; i++) {
            out.append(param[i] + " ");
        }
        out.append("\n\n");
        Bundle b = new Bundle();
        final String so = out.toString();
        Log.d("TUO_RUN", so);
        b.putString("out",so);
        receiver.send(STATUS_RUNNING,b);
        mNotificationManager =
                (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "tuo_channel")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(op) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent nintent = new Intent(getApplicationContext(), OutActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this.getApplicationContext(), id+2, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);

        nintent = new Intent(getApplicationContext(), OutActivity.class);
        nintent.putExtra("stop",android.os.Process.myPid());
        pi = PendingIntent.getActivity(this.getApplicationContext(), id+3, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.addAction(R.drawable.ic_clear,"Cancel All",pi);
/*
        Intent deleteIntent = new Intent(this, TUOIntentService.class);
        deleteIntent.putExtra("stop", true);
        PendingIntent deletePendingIntent = PendingIntent.getService(this,
                0,
                deleteIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.addAction(R.drawable.ic_cancel,"Cancel",deletePendingIntent);*/

        Notification n= mBuilder.build();
        setForegroundAsync(new ForegroundInfo(id+1,n));
        callMain(param);

        Log.d("TUO_IntentService", "onHandleIntentFinished");
        receiver.send(STATUS_FINISHED,Bundle.EMPTY);

        mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
        mBuilder.mActions.clear();
        nintent = new Intent(getApplicationContext(), OutActivity.class);
        final String result = tuo.out.toString();
        nintent.putExtra("text",result);
        //Log.d("TUO_Out",out);
        pi = PendingIntent.getActivity(this.getApplicationContext(), id, nintent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        if(sp.getBoolean("notification_sound",false))mBuilder.setSound(Uri.parse(sp.getString("notification_sound_uri","content://settings/system/notification_sound")));
        if(sp.getBoolean("notification_vibrate",false))mBuilder.setVibrate(new long[] { 1000, 1000});
        if(sp.getBoolean("notification_led",false))mBuilder.setLights(Color.RED, 3000, 3000);
        if(sp.getBoolean("history",false)) {
            //save to output
            String name = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss'.txt'").format(new Date());//save to output
            GlobalData.writeToFile(tuodir + "output/" + name, result + "WTF");
        }
        mNotificationManager.notify(id, mBuilder.build());
        //stopSelf();
        return Result.success();
    }
        public void callMain(String[] args) {tuo.callMain(args);};

    public String stringFromJNI(String s) {return tuo.stringFromJNI(s);};
}
