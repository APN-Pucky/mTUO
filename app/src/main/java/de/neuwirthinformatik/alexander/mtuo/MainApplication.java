package de.neuwirthinformatik.alexander.mtuo;

import android.app.Application;

import androidx.work.Configuration;

public class MainApplication extends Application implements Configuration.Provider {

    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
    }
}
