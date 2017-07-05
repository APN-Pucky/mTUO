
package com.testboost;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;




//-------------------------------------------------------------------
public class JcActivityMain extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback   // for onRequestPermissionsResult()
{

    TextView _Tv;
    //--------------------------------------------------------------------
    static void logIt(String Mesg)
    {
        Log.e("testing boost", "...... " + Mesg);
    }
    //-----------------------------------------------
    static void loadIt(String LibName)
    {
        logIt("......loading " + LibName);
        System.loadLibrary(LibName);
    }
    //-----------------------------------------
    static
    {

        //  loadIt("boost_system");
        // loadIt("boost_chronro");
        loadIt("test_boost");

    }
    //-------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();


        String Str = new String("Java only");
         Str = JcNativeMan.infoStr("Hi from Java ", 3);
         _Tv.setText( _Tv.getText() + "\n" +  Str);

    }
    //-------------------------------------------------
    private void initLayout()
    {
        LinearLayout LayMain = new LinearLayout(this);
        LayMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        LayMain.setGravity(Gravity.CENTER_HORIZONTAL);

        setContentView(LayMain);

        ScrollView _Sv = new ScrollView(this);
        _Sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _Sv.setFillViewport(true);
        LayMain.addView(_Sv);

// Can only add one view to ScrollView so wrap others here
        LinearLayout _Wrap = new LinearLayout(this);
        _Wrap.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _Wrap.setOrientation(LinearLayout.VERTICAL);
        _Sv.addView(_Wrap);



        _Tv = new TextView(this);
            _Wrap.addView(_Tv);
    }



    //----------------------------------------------------



}
