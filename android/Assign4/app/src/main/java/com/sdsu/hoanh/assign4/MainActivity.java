package com.sdsu.hoanh.assign4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    public static final String APP_NAME = "CircleDrawing";

    // last update time.  Use to throttle the accelerometer input rate.
    private long _lastUpdate = 0;

    // maintain the most recent X, Y speed component
    private float _lastXSpeed, _lastYSpeed;

    private SensorManager _senSensorManager;
    private Sensor _senAccelerometer;

    // max speed component for X and Y.  This is to determine how fast the user move the phone
    // This dictate how fast to move the circles
    private float _maxSpeedX = 0;
    private float _maxSpeedY = 0;
    CircleDrawingView circleViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleViewer = (CircleDrawingView)this.findViewById(R.id._circleView);
        _setupAccelerator();
    }

    private void _setupAccelerator()
    {
        _senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        _senAccelerometer = _senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _senSensorManager.registerListener(this, _senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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

        if (id == R.id.action_clear) {
            circleViewer.reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        _senSensorManager.unregisterListener(this, _senAccelerometer);
    }

    @Override
    public void onResume() {
        super.onResume();
        _senSensorManager.registerListener(this, _senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            _calculateNewSpeed(event);
        }
    }

    private void _calculateNewSpeed(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];


        long curTime = System.currentTimeMillis();

        // we take the sample every 1000 ms
        if ((curTime - _lastUpdate) > 1000) {
            long diffTime = (curTime - _lastUpdate);
            _lastUpdate = curTime;

            // after some experimentation, a scale factor of 2000 is reasonable to convert
            // the acceleration of m/sec^2 to pixels
            final int scaleFactor = 20000;
            // determine how fast we move.
            float speedX = ((x - _lastXSpeed) / diffTime * scaleFactor);
            float speedY = ((y - _lastYSpeed) / diffTime * scaleFactor);

            Log.i(MainActivity.APP_NAME, "x speed " + speedX + ", y speed " + speedY);

            boolean newMaxSpeedDetected = false;
            //
            // if we did not move much the assume we are stationary and reset the X and Y
            // components
            //
            if(Math.abs(speedX) < 1.0) {
                _maxSpeedX = 0f;
            }
            else {
                // if we are moving faster than the previous speed, save it and
                // notify the circle view of new speed.
                if(Math.abs(speedX) > Math.abs(_maxSpeedX)){
                    _maxSpeedX = speedX;
                    newMaxSpeedDetected = true;
                }
            }

            // do same thing for the Y component.
            if(Math.abs(speedY) < 1.0) {
                _maxSpeedY = 0f;
            }
            else {
                if(Math.abs(speedY) > Math.abs(_maxSpeedY)){
                    _maxSpeedY = speedY;
                    newMaxSpeedDetected = true;
                }
            }

            // if new speed is detected, tell the viewer to move the circles.
            if(newMaxSpeedDetected) {
                circleViewer.moveCircles((int)_maxSpeedX, (int)_maxSpeedY);
            }

            // save the current speed as last.
            _lastXSpeed = x;
            _lastYSpeed = y;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
