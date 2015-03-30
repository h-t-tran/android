package com.sdsu.hoanh.geoalbum;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;

import java.util.List;


public class PictureDetailActivity extends ActionBarActivity {
    protected PicDetailFragment _picDetailFagment = new PicDetailFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture); //activity_picture_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, _picDetailFagment)
                    .commit();
        }


        Button acceptButton = (Button)this.findViewById(R.id._okPhoto);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save pic
                if(PictureDetailActivity.this._picDetailFagment.savePhoto()) {

                    // once saved return to main activity
                    PictureDetailActivity.this.finish();
                }
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id._cancelPhoto);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureDetailActivity.this.finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_detail, menu);
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
