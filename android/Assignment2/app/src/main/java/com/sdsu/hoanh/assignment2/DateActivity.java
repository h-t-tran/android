package com.sdsu.hoanh.assignment2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.io.FileWriter;


public class DateActivity extends ActionBarActivity {
    public static final String DateSaveFilename = "assign2_date.txt";
    public static final String DATE_ENTRY_KEY = "com.sdsu.hoanh.assignment2.dataEntry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        final EditText et = (EditText) findViewById(R.id.date_entry_textbox);
        _load();

        Button goBtn = (Button)findViewById(R.id.date_accept);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateStr = et.getText().toString();
                if(_save(dateStr))
                {
                    Intent data = new Intent();
                    data.putExtra(DateActivity.DATE_ENTRY_KEY, dateStr);

                    // send data back to the calling activity.
                    DateActivity.this.setResult(RESULT_OK, data);
                    DateActivity.this.finish();
                }

            }
        });
    }

    private boolean _save(String fileContents) {
        boolean res = false;
        String fn = DateSaveFilename;
        try {
            OutputStream file = new BufferedOutputStream(openFileOutput(fn, MODE_PRIVATE));
            file.write(fileContents.getBytes());
            file.close();
            res = true;
        }
        catch (Exception e)
        {
            Toast.makeText(this.getApplicationContext(),
                    "Unable to write to file " + fn + ". Reason " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return res;
    }

    private String _getStoragePath()
    {
        return getFilesDir() + DateSaveFilename;
    }

//    private void _save(String dateStr)
//    {
//        String fn = _getStoragePath();
//        try
//        {
//
//            FileWriter fw = new FileWriter(fn);
//            fw.write(dateStr);
//            fw.close();
//
//            Toast.makeText(this.getApplicationContext(),
//                    "Saved successfully to " + fn,
//                    Toast.LENGTH_LONG).show();
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(this.getApplicationContext(),
//                        "Unable to write to file " + fn + ". Reason " + e.getMessage(),
//                        Toast.LENGTH_LONG).show();
//        }
//    }

    private void _load()
    {
        // read the date from file and set the EditText
        String fn = DateSaveFilename;
        try {

            InputStream file = new BufferedInputStream(openFileInput(fn));
            byte[] data = new byte[file.available()];
            file.read(data, 0, file.available());
            String dateStr = new String(data);

            EditText et = (EditText) findViewById(R.id.date_entry_textbox);
            et.setText(dateStr);

            file.close();
        }
        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),
                    "Unable to read from file " + fn + ". Reason " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date, menu);
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
