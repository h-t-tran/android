package com.sdsu.hoanh.assignment1;

/**
 * Name:        Hoanh Tran
 * Project:     Assigment 1
 * Date:        Feb 1, 2015
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Assignment 1
 */
public class PortraitActivity extends ActionBarActivity {

    private static final String _title = "Assignment1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrait);
        Button clearBnt = (Button)findViewById(R.id.clearButton);
        final TextView tv = (TextView)findViewById(R.id.textView2);


        clearBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
            }
        });

        _log("onCreate()");
    }

    /**
     * called when we relaunch the app from Home
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        _log("onRestart()");
    }

    /**
     * called when we relaunch the app from Home or when we first start the app
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        _log("onStart()");
    }

    /**
     * called when Home is selected
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        _log("onPause()");
    }

    /**
     * called when Home is selected
     */
    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        _log("onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);
        _log("onRestoreInstanceState()");
    }

    /**
     * called when we relaunch the app from Home
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        _log("onResume()");
    }

    private void _log(String msg) {
        TextView tv = (TextView)findViewById(R.id.textView2);
        String newText = tv.getText() + ", " + msg;
        tv.setText(newText);

        Log.i(_title, msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_portrait, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
