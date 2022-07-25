package de.neuwirthinformatik.alexander.mtuo;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class TUOResultReceiver extends ResultReceiver {
    private Receiver mReceiver;
    public TUOResultReceiver(Handler handler, Receiver receiver) {
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

