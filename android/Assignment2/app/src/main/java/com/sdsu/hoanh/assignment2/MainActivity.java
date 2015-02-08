package com.sdsu.hoanh.assignment2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String _dateEntryText = "Date Entry";
    private static final String _keyboardEntryText = "Keyboard Entry";
    private static final String _listSelText = "List Selection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._fillActivitySelectionSpinner();
        if (savedInstanceState == null) {
            _loadDesertListFragment();
        }

        _setupGoButton();
    }

    private void _setupGoButton()
    {
        Button goBtn = (Button)findViewById(R.id.go_button);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we what activity the user wants to navigate to
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                Object selItem = spinner.getSelectedItem();
                if(selItem.toString().equals(_dateEntryText))
                {
                    MainActivity.this._goToDateActivity();
                }
            }
        });
    }

    private void _fillActivitySelectionSpinner()
    {
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        List<String> actArray = new ArrayList<String>();
        actArray.add(_dateEntryText);
        actArray.add(_keyboardEntryText);
        actArray.add(_listSelText);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                actArray);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }

    private void _loadDesertListFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_list_frag_host, new DesertFragment())
                .commit();

    }

    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        // if the result is from the date activity, get the passed back date and display it.
        if(reqCode == Constants.DATE_ACTIVITY_RESULT_CODE)
        {
            String date = data.getStringExtra(DateActivity.DATE_ENTRY_KEY);
            EditText et = (EditText) findViewById(R.id.dataText);
            et.setText(date);

        }
    }
    private void _goToDateActivity()
    {
        Intent intent = new Intent(this, DateActivity.class);
        //intent.putExtra("data1", 123);
        this.startActivityForResult(intent, Constants.DATE_ACTIVITY_RESULT_CODE);
    }

    private void _goToKeyboardActivity()
    {

    }

    private void _goToListActivity()
    {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
