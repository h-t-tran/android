package com.sdsu.hoanh.testcamera;

import android.hardware.Camera;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @SuppressWarnings("deprecation")
    Camera mCamera;

    @SuppressWarnings("deprecation")
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            // display the progress indicator
            //mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    @SuppressWarnings("deprecation")
    private Camera.PictureCallback mJpegCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // create a filename
//            String filename = UUID.randomUUID().toString() + ".jpg";
//            // save the jpeg data to disk
//            FileOutputStream os = null;
//            boolean success = true;
//            try {
//                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
//                os.write(data);
//            } catch (Exception e) {
//                Log.e(TAG, "Error writing to file " + filename, e);
//                success = false;
//            } finally {
//                try {
//                    if (os != null)
//                        os.close();
//                } catch (Exception e) {
//                    Log.e(TAG, "Error closing file " + filename, e);
//                    success = false;
//                }
//            }
//
//            if (success) {
//                // set the photo filename on the result intent
//                if (success) {
//                    Intent i = new Intent();
//                    i.putExtra(EXTRA_PHOTO_FILENAME, filename);
//                    getActivity().setResult(Activity.RESULT_OK, i);
//                } else {
//                    getActivity().setResult(Activity.RESULT_CANCELED);
//                }
//            }
//            getActivity().finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePictureButton = (Button)this.findViewById(R.id.button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpegCallBack);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
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
