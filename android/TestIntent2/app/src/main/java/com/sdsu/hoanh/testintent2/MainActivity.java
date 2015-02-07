package com.sdsu.hoanh.testintent2;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clearBnt = (Button)findViewById(R.id.button);

        final ActionBarActivity ctx = this;
        clearBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "lauching intent", Toast.LENGTH_LONG).show();

                Intent i = new Intent(MainActivity.this, Intent2.class);
                i.putExtra("data1", 123);
                MainActivity.this.startActivity(i);
            }
        });

        Button explicitBtn = (Button)findViewById(R.id._explicitBtn);

        explicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(Intent.ACTION_DIAL);
                try {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE ); // start camera
                    MainActivity.this.startActivity(i);
                }
                catch(Exception e)
                {
                    String msg = e.getMessage();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
