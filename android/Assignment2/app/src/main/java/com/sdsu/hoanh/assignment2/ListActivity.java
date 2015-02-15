package com.sdsu.hoanh.assignment2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ListActivity extends ActionBarActivity {

    private DesertFragment _desertFrag;

    public static final String DESERT_NAME_KEY = "com.sdsu.hoanh.assignment2.ListActivity.DesertNameKey";
    public static final String DESERT_NAME_RESULT_KEY = "com.sdsu.hoanh.assignment2.ListActivity.DesertNameResultKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // get the desert passed in from the calling activity and populate it
        // in the list fragment.
        String desert = this.getIntent().getStringExtra(DESERT_NAME_KEY);
        _loadDesertListFragment(desert); //DesertFragment._donut);

        Button backBtn = (Button)findViewById(R.id.list_activity_button_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _handleBackButton();
            }
        });
    }


    /**
     * create the desert fragment and inject the desert name into it.
     */
    private void _loadDesertListFragment(String desertName)
    {
        _desertFrag = DesertFragment.newInstance(desertName);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_activity_container_list_frag_host, _desertFrag)
                .commit();

    }

    /**
     * pass back the selected desert to the calling activity.
     */
    private void _handleBackButton()
    {
        Intent data = new Intent();
        data.putExtra(ListActivity.DESERT_NAME_RESULT_KEY, _desertFrag.getSelectedDesert());
        // send data back to the calling activity.
        this.setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
