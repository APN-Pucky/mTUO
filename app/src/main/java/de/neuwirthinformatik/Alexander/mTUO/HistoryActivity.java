package de.neuwirthinformatik.Alexander.mTUO;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryActivity extends AppCompatActivity {
    private  HistoryAdapter ada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        File[] files = new File(Global.tuodir() + "output/").listFiles();
        ArrayList<File> fal = new ArrayList<File>();
        for(int i= files.length-1;i>=0;i--)
            fal.add(files[i]);

        final ListView lv = (ListView) findViewById(R.id.history_list);
        ada = new HistoryAdapter(this, fal);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                File f = (File)lv.getItemAtPosition(position);

                final String content = Global.readFile(f.getAbsolutePath());

                Intent nintent = new Intent(getApplicationContext(), OutActivity.class);
                nintent.putExtra("text",content);
                startActivity(nintent);

            }
        });
        lv.setAdapter(ada);
        setupActionBar();
        if(files.length==0 && !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("history",false))Global.alert(this,"Enable History in Settings.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(de.neuwirthinformatik.Alexander.mTUO.R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_history) {
            File[] files = new File(Global.tuodir() + "output/").listFiles();
            for(File f: files)
            {
                f.delete();
            }
            ada.clear();
            /*files = new File(Global.tuodir() + "output/").listFiles();
            String[] theNamesOfFiles = new String[files.length];
            for (int i = 0; i < theNamesOfFiles.length; i++) {
                theNamesOfFiles[i] = files[i].getName().replace("_",":").replace(".txt","");
                ada.insert(theNamesOfFiles[i],ada.getCount());
            }*/
            ada.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public class HistoryAdapter extends ArrayAdapter<File> {

        public HistoryAdapter(Context context, ArrayList<File> users) {

            super(context, 0, users);

        }



        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            // Get the data item for this position

            File file = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view

            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history, parent, false);

            }

            // Lookup view for data population

            TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);

            TextView tv_param = (TextView) convertView.findViewById(R.id.tv_param);

            // Populate the data into the template view using the data object

            String date = file.getName().replace("_",":").replace(".txt","");
            //line += "";
            String fl = Global.readFirstLine(Global.tuodir() + "output/" +file.getName());
            String param ="";
            Log.d("TUO_HIST",fl);
            String[] i = get(fl);
            param += "" + i[0];
            param += " vs " + i[1];
            param += " " + getOP(fl);
            param += " " + getOrder(fl);
            param += " " + getBGE(fl);

            tv_date.setText(date);

            tv_param.setText(param);

            // Return the completed view to render on screen

            return convertView;

        }

        private String[] get(String fl) {
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(fl);
            String[] ret = new String[2];
            m.find();
            m.find();
            ret[0] = m.group(1);
            m.find();
            ret[1] = m.group(1);
            return ret;
        }
        private String getOP(String fl)
        {
            return getLastOf(fl,getResources().getStringArray(R.array.a_operation));
        }
        private String getOrder(String fl)
        {
            return getLastOf(fl,getResources().getStringArray(R.array.a_order));
        }
        private String getBGE(String fl)
        {
            return getLastOf(fl,getResources().getStringArray(R.array.a_effects));
        }

        private String getLastOf(String fl,String[] opt)
        {
            int max = 0;
            String cur="";
            String[] m = opt;
            for(int i =0 ; i < m.length;i++)
            {
                int t = fl.lastIndexOf(" " + m[i] + " ");
                if(max < t){
                    max=t;
                    cur = m[i];
                }
            }
            return cur;
        }

    }
}
