package de.neuwirthinformatik.Alexander.mTUO;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OutActivity extends AppCompatActivity {
    static OutActivity _this = null;
    static TextView tv=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
    }
    @Override
    public void onStart() {
        super.onStart();

        tv = (TextView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.tv_out);
        tv.setText(MainActivity.out);
        _this = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        tv=null;
        _this = null;
    }
}
