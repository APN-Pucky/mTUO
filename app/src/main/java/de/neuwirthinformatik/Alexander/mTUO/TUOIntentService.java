package de.neuwirthinformatik.Alexander.mTUO;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.Serializable;

public class TUOIntentService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static StringBuilder out = new StringBuilder();
    private ResultReceiver receiver;
    //public static TUOIntentService _this;



    public TUOIntentService() {
        super("TUOIntentService");

        Log.d("TUO_IntentService", "loading TUO lib");
        System.loadLibrary("tuo");
        Log.d("TUO_IntentService", "load TUO lib");
        //_this = this;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotificationManager;
        int id = (int)System.currentTimeMillis();
        Log.d("TUO_PROC_IntentService", "onHandleIntent");
        receiver = intent.getParcelableExtra("receiver");
        String[] param = intent.getStringArrayExtra("param");
        String op = intent.getStringExtra("operation");
        out.setLength(0);
        out.append("./");
        for (int i = 0; i < param.length; i++) {
            out.append(param[i] + " ");
        }
        out.append("\n\n");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "TUO_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("TUO_NOTIFICATION_CHANNEL_DISCRIPTION");
            (mNotificationManager).createNotificationChannel(channel);
        }
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(op) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent nintent = new Intent(getApplicationContext(), OutActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this, id+2, nintent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);

        nintent = new Intent(getApplicationContext(), OutActivity.class);
        nintent.putExtra("stop",android.os.Process.myPid());
        pi = PendingIntent.getActivity(this, id+3, nintent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.addAction(R.drawable.ic_cancel,"Cancel All",pi);
/*
        Intent deleteIntent = new Intent(this, TUOIntentService.class);
        deleteIntent.putExtra("stop", true);
        PendingIntent deletePendingIntent = PendingIntent.getService(this,
                0,
                deleteIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.addAction(R.drawable.ic_cancel,"Cancel",deletePendingIntent);*/

        startForeground(id+1,mBuilder.build());
        callMain(param);

        Log.d("TUO_IntentService", "onHandleIntentFinished");
        receiver.send(STATUS_FINISHED,Bundle.EMPTY);

        mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
        mBuilder.mActions.clear();
        nintent = new Intent(getApplicationContext(), OutActivity.class);
        nintent.putExtra("text",out.toString());
        //Log.d("TUO_Out",out);
        pi = PendingIntent.getActivity(this, id, nintent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        stopForeground(true);
        mNotificationManager.notify(id, mBuilder.build());
        //stopSelf();
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int ret = super.onStartCommand(intent,flags,startId);
        if(intent.getBooleanExtra("stop",false))
        {
            Log.d("TUO_Out","stop");
            stopForeground(true);
            stopSelf();
        }
        return ret;
    }*/

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

    public static class TUOResultReceiver extends ResultReceiver {
        private Receiver mReceiver;
        public TUOResultReceiver(Handler handler,Receiver receiver) {
            super(handler);
            mReceiver = receiver;
        }
        public interface Receiver {
            public void onReceiveResult(int resultCode, Bundle resultData);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (mReceiver != null) {
                mReceiver.onReceiveResult(resultCode,resultData);
            }
        }
    }

    public native void callMain(String[] args);

    public native String stringFromJNI(String s);
}
