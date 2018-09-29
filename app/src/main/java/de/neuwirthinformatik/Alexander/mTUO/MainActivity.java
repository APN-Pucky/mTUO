package de.neuwirthinformatik.Alexander.mTUO;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

public class MainActivity extends AppCompatActivity {
    public static final int CARD_SECTIONS_COUNT = 17;
    private String tuodir;
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
        tuodir = Environment.getExternalStorageDirectory() + "/TUO/";
        File directory = new File(tuodir);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        final Button button_sim = findViewById(R.id.RunSim);
        button_sim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callMain(new String[]{"tuo","Mission#140","Mission#140","sim", "1000","-t", "2", "prefix", tuodir});
            }
        });

        final Button button_xml = findViewById(R.id.updatexml);
        button_xml.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               updateXML(true);
            }
        });
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

    public void messageMe(String msg) {
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(de.neuwirthinformatik.Alexander.mTUO.R.id.sample_text);
        tv.setText(tv.getText() + msg);
    }

    public void updateXML()  {updateXML(false);}
    public void updateXML(boolean dev)
    {
        final String tyrant_url = (dev?"http://mobile-dev.tyrantonline.com/assets/":"http://mobile.tyrantonline.com/assets/");
        //XML
       Log.v("MainActivity","Downloading new XMLs ...");
        for(int i = 1; i <= CARD_SECTIONS_COUNT;i++ )
        {
            final String sec = "cards_section_" + i + ".xml";
            Wget.wGetAsync(tuodir + "data/" + sec, tyrant_url + sec);
        }
        final String[] arr = new String[] {"fusion_recipes_cj2","missions","levels","skills_set"};
        for(int i =0; i < arr.length;i++)
        {
            final String sec = arr[i] + ".xml";
            Wget.wGetAsync(tuodir +"data/" + sec, tyrant_url + sec);
        }
        Wget.wGetAsync(tuodir +"data/" + "raids.xml", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/raids.xml");
        Wget.wGetAsync(tuodir +"data/" + "bges.txt", "https://raw.githubusercontent.com/APN-Pucky/tyrant_optimize/merged/data/bges.txt");

    }

    /**
     * A native method that is implemented by the 'tuo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String s);
    public native void callMain(String[] args);
}
