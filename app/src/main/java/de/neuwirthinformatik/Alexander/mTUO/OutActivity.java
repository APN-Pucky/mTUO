package de.neuwirthinformatik.Alexander.mTUO;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class OutActivity extends AppCompatActivity {
    static OutActivity _this = null;
    static TextView tv=null;
    static ScrollView sv=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TUO_OutActivity","onCreate");
        setContentView(R.layout.activity_out);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("TUO_OutActivity","onStart");



        tv = (TextView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.tv_out);
        tv.setMovementMethod(new ScrollingMovementMethod());
        sv = (ScrollView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.sv_out);
        sv.post(new Runnable()
        {
            public void run()
            {
                sv.fullScroll(View.FOCUS_DOWN);
            }
        });
        if(getIntent().hasExtra("text"))
        {
            Log.d("TUO_OutActivity","text");
            tv.setText(getIntent().getStringExtra("text"));
        }
        else{
            Log.d("TUO_OutActivity","out");
            tv.setText(MainActivity.out);
        }
        if(getIntent().hasExtra("stop"))
        {
            Log.d("TUO_OutActivity","stop");
            GlobalData.stopAllTUO(this);

        }
        _this = this;

    }

    public void setText(final String out)
    {
        if (OutActivity.tv != null) {
            OutActivity._this.runOnUiThread(new Runnable() {
                public void run() {
                    OutActivity.tv.setText(out);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TUO_OutActivity","onStop");
        tv=null;
        _this = null;
        sv= null;
    }
}
