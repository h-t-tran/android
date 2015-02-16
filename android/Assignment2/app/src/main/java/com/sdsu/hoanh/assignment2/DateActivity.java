package com.sdsu.hoanh.assignment2;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

/**
 * This activity experiments with Date entry and file IO
 */
public class DateActivity extends ActionBarActivityAbstract {
    public static final String DateSaveFilename = "assign2_date.txt";
    public static final String DATE_ENTRY_KEY = "com.sdsu.hoanh.assignment2.dataEntry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        final EditText et = (EditText) findViewById(R.id.date_entry_textbox);
        _load();
        showAppIcon();

        Button goBtn = (Button)findViewById(R.id.date_accept);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateActivity.this._saveAndGoBackToHomeActivity();
            }
        });
    }

    private void _saveAndGoBackToHomeActivity()
    {
        EditText et = (EditText) findViewById(R.id.date_entry_textbox);
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

    /**
     * handler for context menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            // pass back null data to signal calling activity nothing is changed
            this.setResult(RESULT_OK, null);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Save the string to disk
     * @param fileContents - the content to save
     * @return true if success, false if failed.
     */
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

    /**
     * Load the data from file.
     */
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

}
