package de.neuwirthinformatik.alexander.mTUO;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

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
    String[] param;
    String name;
    String op;
    static {
        System.loadLibrary("tuo");
    }

    public TUO(Context c,String[] param, String op,ResultReceiver receiver) {
        this.context = c;
        this.receiver = receiver;
        this.param = param;
        this.op = op;
        tuodir = GlobalData.tuodir();

        name = new SimpleDateFormat("yyyy-MM-dd hh_mm_ss'.txt'").format(new Date());//save to output
        out.setLength(0);
        out.append("./").append(param[0]).append(" ")
                .append("\"").append(param[1]).append("\" ")
        .append("\"")
        .append(param[2]).append("\" ");
        for (int i = 3; i < param.length; i++) {
            out.append(param[i]).append(" ");
        }
        out.append("\n\n");
        Bundle b = new Bundle();
        final String so = out.toString();
        Log.d("TUO_RUN", so);
        b.putString("out",so);
        receiver.send(STATUS_RUNNING,b);

    }


    public void run() {

        callMain(param);


        Log.d("TUO_IntentService", "onHandleIntentFinished");
        Bundle        b = new Bundle();
        final String sos = out.toString();
        b.putString("name",name);
        b.putString("out",sos);
        receiver.send(STATUS_FINISHED,b);
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
        if (  OutActivity._this != null && OutActivity._this.tv != null) {
            OutActivity._this.runOnUiThread(new Runnable() {
                public void run() {
                    OutActivity._this.tv.setText(out);
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
