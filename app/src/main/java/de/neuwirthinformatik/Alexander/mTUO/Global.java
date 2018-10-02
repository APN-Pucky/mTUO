package de.neuwirthinformatik.Alexander.mTUO;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class Global
{
    public static void stopAllTUO(Activity a)
    {
        Log.d("TUO_GLOBAL","stop all tuo");

        a.stopService(new Intent(a,TUOIntentService.class));
        //android.os.Process.killProcess(c.getIntent().getIntExtra("stop",0));
        //TUOIntentService._this.stopForeground(true);
        //TUOIntentService._this.stopSelf();

        ActivityManager manager = (ActivityManager)a.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = manager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo s : services)
        {
            if(s.processName.equals("de.neuwirthinformatik.Alexander.mTUO:tuo"))
                android.os.Process.killProcess(s.pid);
            Log.d("TUO_PROC",s.processName);
        }
    }
}
