package de.neuwirthinformatik.Alexander.mTUO;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GlobalData {


    public static void error(Context c, String title,String msg)
    {
        new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }
    public static void alert(Context c,String msg)
    {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
    public static void stopAllTUO(Activity a) {
        Log.d("TUO_GLOBAL", "stop all tuo");

        a.stopService(new Intent(a, TUOIntentService.class));
        //android.os.Process.killProcess(c.getIntent().getIntExtra("stop",0));
        //TUOIntentService._this.stopForeground(true);
        //TUOIntentService._this.stopSelf();

        ActivityManager manager = (ActivityManager) a.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo s : services) {
            if (s.processName.equals("de.neuwirthinformatik.Alexander.mTUO:tuo"))
                android.os.Process.killProcess(s.pid);
            Log.d("TUO_PROC", s.processName);
        }
    }

    public static void writeToFile(String file, String data) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(data.getBytes());
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                Log.e("Exception", "File close failed: " + e.toString());
            }
        }
    }

    public static String readFirstLine(String file)
    {
        FileInputStream fin = null;
        String ret = "";
        try {
            fin = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            ret = reader.readLine();
        } catch (Exception e) {

            Log.e("Exception", "File read failed: " + e.toString());
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException e) {
                Log.e("Exception", "File close failed: " + e.toString());
            }
        }
        return ret;
    }

    public static String readFile(String file) {
        FileInputStream fin = null;
        String ret = "";
        try {
            fin = new FileInputStream(file);
            ret = convertStreamToString(fin);
        } catch (Exception e) {

            Log.e("Exception", "File read failed: " + e.toString());
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException e) {
                Log.e("Exception", "File close failed: " + e.toString());
            }
        }
        return ret;
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String tuodir()
    {
        return Environment.getExternalStorageDirectory() + "/TUO/";
    }

}
