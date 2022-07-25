package de.neuwirthinformatik.alexander.mTUO;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InActivity extends AppCompatActivity {
    static InActivity _this = null;
    String file = "";
    EditText tv=null;
    ScrollView sv=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TUO_InActivity","onCreate");
        setContentView(R.layout.activity_in);
        setupActionBar();
        tv = (EditText) findViewById(R.id.et_in);
        tv.setMovementMethod(new ScrollingMovementMethod());
        sv = (ScrollView) findViewById(R.id.sv_in);
        sv.post(new Runnable()
        {
            public void run()
            {
                sv.fullScroll(View.FOCUS_DOWN);
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(de.neuwirthinformatik.alexander.mTUO.R.menu.menu_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Log.d("TUO_InActivity","Save");
            String text = tv.getText().toString();
            GlobalData.writeToFile(file ,text );
            tv.setSelection(tv.getText().length());
            Log.d("TUO_InActivity",text);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("TUO_InActivity","onStart");

        if(getIntent().hasExtra("file"))
        {
            Log.d("TUO_InActivity","file");
            file = getIntent().getStringExtra("file");
            String in  = GlobalData.readFile(file);
            Log.d("TUO_InActivity",in);
            tv.setText(in);
        }
        else {
            file = "";
        }

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
        sv= null;
    }
}
