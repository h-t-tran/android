package com.sdsu.hoanh.testaccelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.SensorEventListener;

public class MainActivity extends ActionBarActivity  implements SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float maxSpeedX = 0;
    private float maxSpeedY = 0;
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1000) {
                Log.i("FOOBAR", "x = " + x + ", y = " + y + ", z = " + z);

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
                float speedX = ((x - last_x) / diffTime * 10000);
                float speedY = ((y - last_y) / diffTime * 10000);
//Log.i("FOOBAR", "speed " + speed);
                Log.i("FOOBAR", "x speed " + speedX + ", y speed "+ speedY);

                if(Math.abs(speedX) < 1.0) {
                    maxSpeedX = 0f;
                }
                else {
                    if(Math.abs(speedX) > Math.abs(maxSpeedX)){
                        maxSpeedX = speedX;
                    }
                }
                if(Math.abs(speedY) < 1.0) {
                    maxSpeedY = 0f;
                }
                else {
                    if(Math.abs(speedY) > Math.abs(maxSpeedY)){
                        maxSpeedY = speedY;
                    }
                }

                Log.i("FOOBAR", "maxSpeedX " + maxSpeedX + ", maxSpeedY "+ maxSpeedX);

                if (speed > SHAKE_THRESHOLD) {

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
