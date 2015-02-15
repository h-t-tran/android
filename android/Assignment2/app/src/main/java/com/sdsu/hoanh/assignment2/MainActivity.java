package com.sdsu.hoanh.assignment2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
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

    private static String _dateEntryText = "Date Entry";
    private static final String _keyboardEntryText = "Keyboard Entry";
    private static final String _listSelText = "List Selection";
    public static final String KEYBOARD_DATA_KEY = "com.sdsu.hoanh.assignment2.keyboard_data_key";

    private String _selectDesert = null;
    private DesertFragment _desertFrag;

    @TargetApi(11)
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
                else if(selItem.toString().equals(_keyboardEntryText))
                {
                    MainActivity.this._goToKeyboardActivity();
                }
                else if(selItem.toString().equals(_listSelText))
                {
                    MainActivity.this._goToListActivity();
                }
            }
        });

        //this.registerForContextMenu(goBtn);
    }


    private void _fillActivitySelectionSpinner()
    {
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        List<String> actArray = new ArrayList<String>();
        actArray.add(_listSelText);
        actArray.add(_keyboardEntryText);
        actArray.add(_dateEntryText);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                actArray);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }

    /**
     * Load the List fragment and initialize it
     */
    private void _loadDesertListFragment()
    {
        _desertFrag = DesertFragment.newInstance(_selectDesert);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_list_frag_host, _desertFrag)
                .commit();

    }

    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        if(data == null)
        {
            return;
        }
        // if the result is from the date activity, get the passed back date and display it.
        if(reqCode == Constants.DATE_ACTIVITY_RESULT_CODE)
        {
            String date = data.getStringExtra(DateActivity.DATE_ENTRY_KEY);
            EditText et = (EditText) findViewById(R.id.dataText);
            et.setText(date);
        }
        else if(reqCode == Constants.LIST_ACTIVITY_RESULT_CODE)
        {
            String desert = data.getStringExtra(ListActivity.DESERT_NAME_RESULT_KEY);
            // update the listview with the new desert.
            _desertFrag.setSelectedDesert(desert);
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
        Intent intent = new Intent(this, KeyboardActivity.class);
        // add data to the intent for the keyboard activity
        EditText et = (EditText) findViewById(R.id.dataText);
        intent.putExtra(KEYBOARD_DATA_KEY, et.getText().toString());
        this.startActivity(intent);
    }

    private void _goToListActivity()
    {
        String selDesert = _desertFrag.getSelectedDesert();
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.DESERT_NAME_KEY, selDesert);
        this.startActivityForResult(intent, Constants.LIST_ACTIVITY_RESULT_CODE);
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
        if (id == R.id.action_goto_date) {
            _goToDateActivity();
        }
        else if (id == R.id.action_goto_list) {
            _goToListActivity();
        }
        else if (id == R.id.action_goto_keyboard) {
            _goToKeyboardActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
    }
}
