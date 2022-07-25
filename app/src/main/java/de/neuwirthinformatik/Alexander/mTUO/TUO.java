package de.neuwirthinformatik.Alexander.mTUO;

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

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TUO {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public StringBuilder out = new StringBuilder();
    public static String tuodir;
    Context context;
    ResultReceiver receiver;
    static {
        System.loadLibrary("tuo");
    }

    public TUO(Context c,ResultReceiver receiver) {
        this.context = c;
        this.receiver = receiver;
        tuodir = GlobalData.tuodir();
    }

    public void run(String[] param, String op) {
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); ;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        int id = (int)System.currentTimeMillis();
        Log.d("TUO_PROC_IntentService", "onHandleIntent");

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

        mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "tuo_channel")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(op) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent nintent = new Intent(context.getApplicationContext(), OutActivity.class);

        PendingIntent pi = PendingIntent.getActivity(context, id+2, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);

        nintent = new Intent(context.getApplicationContext(), OutActivity.class);
        nintent.putExtra("stop",android.os.Process.myPid());
        pi = PendingIntent.getActivity(context, id+3, nintent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.addAction(R.drawable.ic_clear,"Cancel All",pi);

        // OLD
        //startForeground(id+1,mBuilder.build());
        callMain(param);

        Log.d("TUO_IntentService", "onHandleIntentFinished");
        receiver.send(STATUS_FINISHED,Bundle.EMPTY);

        mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
        mBuilder.mActions.clear();
        nintent = new Intent(context.getApplicationContext(), OutActivity.class);
        final String result = out.toString();
        nintent.putExtra("text",result);
        //Log.d("TUO_Out",out);
        pi = PendingIntent.getActivity(context, id, nintent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        if(sp.getBoolean("notification_sound",false))mBuilder.setSound(Uri.parse(sp.getString("notification_sound_uri","content://settings/system/notification_sound")));
        if(sp.getBoolean("notification_vibrate",false))mBuilder.setVibrate(new long[] { 1000, 1000});
        if(sp.getBoolean("notification_led",false))mBuilder.setLights(Color.RED, 3000, 3000);
        // OLD
        // stopForeground(true);
        if(sp.getBoolean("history",false)) {
            //save to output
            String name = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss'.txt'").format(new Date());
            GlobalData.writeToFile(tuodir + "output/" + name, result);
        }
        mNotificationManager.notify(id, mBuilder.build());
    }

    // In a worker environment this could update the notifications as well (preview result, lastline)
    // TODO
    public void output(String msg) {
        // Forward tuo output
        out.append(msg);
        Log.d("TUO_IntentTOUT", msg);
        Bundle b = new Bundle();
        b.putString("out",out.toString());
        receiver.send(STATUS_RUNNING,b);
            Log.d("TUO_IntentTOUT", "bundle sent");
        if (OutActivity.tv != null && OutActivity._this != null) {
            OutActivity._this.runOnUiThread(new Runnable() {
                public void run() {
                    OutActivity.tv.setText(out);
                }
            });
        }
    }
   /**
     * A native method that is implemented by the 'tuo' native library,
     * which is packaged with this application.
     */

    public native void callMain(String[] args);

    public native String stringFromJNI(String s);
}
