package com.sdsu.hoanh.geoalbum;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sdsu.hoanh.geoalbum.Model.GpsProvider;
import com.sdsu.hoanh.geoalbum.Model.Photo;
import com.sdsu.hoanh.geoalbum.Model.PhotoDatabaseHelper;

import java.util.Date;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUpHandlers();

        // bootstrap the database with this context.
        PhotoDatabaseHelper database = PhotoDatabaseHelper.createInstance(this);
        //_testDbInsert(database);

        // start GPS monitoring
        GpsProvider gpsProvider = new GpsProvider();
        gpsProvider.start(this);
    }

    private void _testDbInsert(PhotoDatabaseHelper database) {
        Photo p = new Photo();
        p.setImagePath("the path2" + (new Date()).toString());
        p.setLon(1.0);
        p.setLat(2.0);
        p.setDesc("desc");
        p.setTitle("title "+ (new Date()).toString());
        p.setDate(new Date());
        database.insertOrUpdateTeacher(p);

        database.getAllPhotos();
    }


    private void wireUpHandlers()
    {
        Button showMapButton = (Button)this.findViewById(R.id._btnMap);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showMap();
            }
        });


        Button takePicButton = (Button)this.findViewById(R.id._btnTakePics);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.takePicture();
            }
        });
    }
    private void showMap()
    {
        Intent i = new Intent(this, MapsActivity.class);
        this.startActivity(i);

    }

    private void showPicListing()
    {

    }

    private void takePicture()
    {
        Intent i = new Intent(this, TakePictureActivity.class);
        this.startActivity(i);

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
