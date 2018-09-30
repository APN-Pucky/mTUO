package de.neuwirthinformatik.Alexander.mTUO;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.ArrayAdapter;
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
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final int CARD_SECTIONS_COUNT = 20;
    public static String out = "";
    private String tuodir;

    NotificationManager mNotificationManager;
    SharedPreferences preferences;

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
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        tuodir = Environment.getExternalStorageDirectory() + "/TUO/";
        File directory = new File(tuodir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        initButtons();

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        loadPrefs();
    }

    private void initButtons()
    {
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

        final Button button_owned = findViewById(R.id.b_ownedcards);
        button_owned.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editFile(tuodir + "data/ownedcards.txt");
            }
        });

        final Button button_custom = findViewById(R.id.b_customdecks);
        button_custom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editFile(tuodir + "data/customdecks.txt");
            }
        });
    }

    private void loadPrefs()
    {
        String mydeck = preferences.getString("mydeck", null);
        if(mydeck!=null) ((EditText) findViewById(R.id.et_mydeck)).setText(mydeck);
        String enemydeck = preferences.getString("enemydeck", null);
        if(enemydeck!=null) ((EditText) findViewById(R.id.et_enemydeck)).setText(enemydeck);
        String myfort = preferences.getString("myfort", null);
        if(myfort!=null) ((EditText) findViewById(R.id.et_myfort)).setText(myfort);
        String enemyfort = preferences.getString("enemyfort", null);
        if(enemyfort!=null) ((EditText) findViewById(R.id.et_enemyfort)).setText(enemyfort);

        String flags = preferences.getString("flags", null);
        if(flags!=null) ((EditText) findViewById(R.id.et_flags)).setText(flags);
        String fund = preferences.getString("fund", null);
        if(fund!=null) ((EditText) findViewById(R.id.et_fund)).setText(fund);
        String threads = preferences.getString("threads", null);
        if(threads!=null) ((EditText) findViewById(R.id.et_threads)).setText(threads);
        String iterations1 = preferences.getString("iterations1", null);
        if(iterations1!=null) ((EditText) findViewById(R.id.et_iterations1)).setText(iterations1);


        String mode = preferences.getString("mode", null);
        if(mode!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_mode));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition(mode));}
        String order = preferences.getString("order", null);
        if(order!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_order));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition(order));}
        String operation = preferences.getString("operation", null);
        if(operation!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_operation));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition(operation));}
        String effect = preferences.getString("effect", null);
        if(effect!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_effect));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition(effect));}
        String endgame = preferences.getString("endgame", null);
        if(endgame!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_endgame));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition(endgame));}
        String  dominion = preferences.getString("dominion", null);
        if(dominion!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_dominion));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition( dominion));}
        String  strategy = preferences.getString("strategy", null);
        if(strategy!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_strategy));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition( strategy));}
        String  monofaction = preferences.getString("monofaction", null);
        if(monofaction!=null) {Spinner s = ((Spinner) findViewById(R.id.sp_monofaction));s.setSelection(((ArrayAdapter)s.getAdapter()).getPosition( monofaction));}



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

    private void editFile(String abs_file)
    {
        try {
            File yourFile = new File(abs_file);
            yourFile.getParentFile().mkdirs();
            yourFile.createNewFile(); // if file already exists will do nothing
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_EDIT);
        Uri uri = Uri.parse("file://"+abs_file);
        intent.setDataAndType(uri, "text/plain");
        startActivity(intent);
    }

    public void messageMe(String msg) {
        // Forward tuo output
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
        String order = ((Spinner) findViewById(R.id.sp_order)).getSelectedItem().toString();
        String operation = ((Spinner) findViewById(R.id.sp_operation)).getSelectedItem().toString();
        String effect = ((Spinner) findViewById(R.id.sp_effect)).getSelectedItem().toString();
        String endgame = ((Spinner) findViewById(R.id.sp_endgame)).getSelectedItem().toString();
        String dominion = ((Spinner) findViewById(R.id.sp_dominion)).getSelectedItem().toString();
        String strategy = ((Spinner) findViewById(R.id.sp_strategy)).getSelectedItem().toString();
        String mono = ((Spinner) findViewById(R.id.sp_monofaction)).getSelectedItem().toString();


        String fund = ((EditText) findViewById(R.id.et_fund)).getText().toString();
        String threads = ((EditText) findViewById(R.id.et_threads)).getText().toString();
        String iterations = ((EditText) findViewById(R.id.et_iterations1)).getText().toString();
        //String iterations2 = ((EditText) findViewById(R.id.et_iterations2)).getText().toString();
        //String iterations3 = ((EditText) findViewById(R.id.et_iterations3)).getText().toString();

        String[] pre = new String[]{"tuo", mydeck, enemydeck, "prefix", tuodir, "yf", myfort, "ef", enemyfort, "-t", threads, "endgame", endgame, "fund", fund, "-e", effect, mode,dominion, "strategy",strategy, "mono", mono, order,operation, iterations};

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mNotificationManager.cancelAll();
        mNotificationManager.cancel(0);
        mNotificationManager.cancel(1);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("mydeck",((EditText) findViewById(R.id.et_mydeck)).getText().toString() );
        editor.putString("enemydeck",((EditText) findViewById(R.id.et_enemydeck)).getText().toString() );
        editor.putString("myfort",((EditText) findViewById(R.id.et_myfort)).getText().toString() );
        editor.putString("enemyfort",((EditText) findViewById(R.id.et_enemyfort)).getText().toString() );

        editor.putString("flags",((EditText) findViewById(R.id.et_flags)).getText().toString() );
        editor.putString("fund",((EditText) findViewById(R.id.et_fund)).getText().toString() );
        editor.putString("threads",((EditText) findViewById(R.id.et_threads)).getText().toString() );
        editor.putString("iterations1",((EditText) findViewById(R.id.et_iterations1)).getText().toString() );

        editor.putString("dominion",((Spinner) findViewById(R.id.sp_dominion)).getSelectedItem().toString());
        editor.putString("mode",((Spinner) findViewById(R.id.sp_mode)).getSelectedItem().toString());
        editor.putString("operation",((Spinner) findViewById(R.id.sp_operation)).getSelectedItem().toString());
        editor.putString("monofaction",((Spinner) findViewById(R.id.sp_monofaction)).getSelectedItem().toString());
        editor.putString("strategy",((Spinner) findViewById(R.id.sp_strategy)).getSelectedItem().toString());
        editor.putString("order",((Spinner) findViewById(R.id.sp_order)).getSelectedItem().toString());
        editor.putString("endgame",((Spinner) findViewById(R.id.sp_endgame)).getSelectedItem().toString());
        editor.putString("effect",((Spinner) findViewById(R.id.sp_effect)).getSelectedItem().toString());
        editor.commit();
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
