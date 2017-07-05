package com.testboost;

import android.app.Application;
import android.content.Context;


public class JcApplication extends Application
{
    private static Context _Context;
    public static Context getContext(){ return _Context;}

    @Override
    public void onCreate()
    {
        super.onCreate();
        _Context = getApplicationContext();


    }
}

