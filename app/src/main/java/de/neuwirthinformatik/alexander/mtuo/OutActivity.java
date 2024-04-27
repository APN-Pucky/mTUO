package de.neuwirthinformatik.alexander.mtuo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class OutActivity extends AppCompatActivity {
    static OutActivity _this = null;
    TextView tv=null;
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



        tv = (TextView) findViewById(R.id.tv_out);


        if(getIntent().hasExtra("text"))
        {
            Log.d("TUO_OutActivity","text");
            tv.setText(getIntent().getStringExtra("text"));
        }
        else{
            Log.d("TUO_OutActivity","out");
            tv.setText(MainActivity.out);
        }
        tv.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Layout l = tv.getLayout();
                        final int scrollAmount = l.getLineTop(tv.getLineCount()) - tv.getHeight();
                        if (scrollAmount > 0)
                            tv.scrollTo(0, scrollAmount);
                        else
                            tv.scrollTo(0, 0);
                    }
                }
        );

        _this = this;

    }

    public void setText(final String out)
    {
        if (tv != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    tv.setText(out);
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
    }
}
