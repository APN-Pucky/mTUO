package de.neuwirthinformatik.Alexander.mTUO;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final int CARD_SECTIONS_COUNT = 20;
    public static String out = "";
    private String tuodir;

    NotificationManager mNotificationManager;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("tuo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(de.neuwirthinformatik.Alexander.mTUO.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.toolbar);
        setSupportActionBar(toolbar);

        //Request Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
        tuodir = Environment.getExternalStorageDirectory() + "/TUO/";
        File directory = new File(tuodir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        final Button button_sim = findViewById(R.id.b_runsim);
        button_sim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startSIM();
            }
        });

        final Button button_xml = findViewById(R.id.b_updatexml);
        button_xml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateXML(true);
            }
        });

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(de.neuwirthinformatik.Alexander.mTUO.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == de.neuwirthinformatik.Alexander.mTUO.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void messageMe(String msg) {
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.sample_text);
        //tv.setText(tv.getText() + msg);
        //TextView tv = (TextView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.tv_out);
        //tv.setText(tv.getText() + msg);
        out += msg;
        if (OutActivity.tv != null && OutActivity._this != null) {
            OutActivity._this.runOnUiThread(new Runnable() {
                public void run() {
                    OutActivity.tv.setText(out);
                }
            });
        }
    }

    private void startSIM() {
        //grab info
        String mydeck = ((EditText) findViewById(R.id.et_mydeck)).getText().toString();
        String enemydeck = ((EditText) findViewById(R.id.et_enemydeck)).getText().toString();
        String myfort = ((EditText) findViewById(R.id.et_myfort)).getText().toString();
        String enemyfort = ((EditText) findViewById(R.id.et_enemyfort)).getText().toString();

        String mode = ((Spinner) findViewById(R.id.sp_mode)).getSelectedItem().toString();
        String operation = ((Spinner) findViewById(R.id.sp_operation)).getSelectedItem().toString();
        String effect = ((Spinner) findViewById(R.id.sp_effect)).getSelectedItem().toString();
        String endgame = ((Spinner) findViewById(R.id.sp_endgame)).getSelectedItem().toString();


        String fund = ((EditText) findViewById(R.id.et_fund)).getText().toString();
        String threads = ((EditText) findViewById(R.id.et_threads)).getText().toString();
        String iterations = ((EditText) findViewById(R.id.et_iterations1)).getText().toString();

        String[] pre = new String[]{"tuo", mydeck, enemydeck, "prefix", tuodir, "yf", myfort, "ef", enemyfort, operation, iterations, "-t", threads, "endgame", endgame, "fund", fund, "-e", effect, mode};

        //parse flags field
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(((EditText) findViewById(R.id.et_flags)).getText().toString());
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        String[] post = list.toArray(new String[]{});
        final String[] param = new String[pre.length + post.length];
        out = "./";
        for (int i = 0; i < param.length; i++) {
            param[i] = i < pre.length ? pre[i] : post[i - pre.length];
            out += param[i] + " ";
        }
        out += "\n\n";

        //Notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(operation) // title for notification
                .setContentText("Running")// message for notification
                .setOngoing(true);
        Intent intent = new Intent(getApplicationContext(), OutActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());

        AsyncTask.execute(new Runnable() {
            public void run() {
                callMain(param);
                mBuilder.setOngoing(false).setAutoCancel(true).setContentText("Done");
                mNotificationManager.notify(0, mBuilder.build());
            }
        });
        //mNotificationManager.cancel(0);
    }

    void showNotification(String title, String content) {

    }

    public void updateXML() {
        updateXML(false);
    }

    public void updateXML(final boolean dev) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        final String[] arr = new String[]{"fusion_recipes_cj2", "missions", "levels", "skills_set"};
        final int max_p = CARD_SECTIONS_COUNT+arr.length+2;

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("XML") // title for notification
                .setContentText("Downloading")// message for notification
                .setOngoing(true)
                .setProgress(max_p,1,false);
        AsyncTask.execute(new Runnable() {
            public void run() {
                final String tyrant_url = (dev ? "http://mobile-dev.tyrantonline.com/assets/" : "http://mobile.tyrantonline.com/assets/");
                //XML
                Wget.Status status = Wget.Status.Success;
                Log.v("MainActivity", "Downloading new XMLs ...");
                int i = 1;
                int p =0;
                while (status == Wget.Status.Success)
                {
                    final String sec = "cards_section_" + i + ".xml";
                    status=Wget.wGet(tuodir + "data/" + sec, tyrant_url + sec);
                    i++;p++;
                    mNotificationManager.notify(1, mBuilder.setProgress(max_p,p,false).build());
                }
                for (i = 0; i < arr.length; i++) {
                    final String sec = arr[i] + ".xml";
                    Wget.wGet(tuodir + "data/" + sec, tyrant_url + sec);
                    p++;
                    mNotificationManager.notify(1, mBuilder.setProgress(max_p,p,false).build());
                }
                Wget.wGet(tuodir + "data/" + "raids.xml", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/raids.xml");
                p++;
                mNotificationManager.notify(1, mBuilder.setProgress(max_p,p,false).build());
                Wget.wGet(tuodir + "data/" + "bges.txt", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/bges.txt");
                mNotificationManager.notify(1, mBuilder.setProgress(max_p,max_p,false).build());
                mNotificationManager.cancel(1);
            }
        });
    }

    /**
     * A native method that is implemented by the 'tuo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String s);

    public native void callMain(String[] args);
}
