package com.sdsu.hoanh.assignment2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position,
//                                       long id) {
//                Object obj = spinner.getSelectedItem();
//                String label = parent.getItemAtPosition(position).toString();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });

    }

    private void _loadDesertListFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_list_frag_host, new DesertFragment())
                .commit();

    }

    private void _goToDateActivity()
    {

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
