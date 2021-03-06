package com.sdsu.hoanh.assignment2;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This activity experiments with the keyboard display and entry.
 */
public class KeyboardActivity extends ActionBarActivityAbstract {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        // set the auto resize for this activity.
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        String dataFromMain = this.getIntent().getStringExtra(MainActivity.KEYBOARD_DATA_KEY);
        final EditText txt = (EditText)findViewById(R.id.keyboard_top_text);
        txt.setText(dataFromMain);

        // After lots of trials and googling, I need to start
        // a thread before we can show the soft keyboard.
        // Issue: for some reason, sometimes the keyboard does not show up the first time we launch this
        // activity, Subsequent launches work fine. why??
        txt.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardActivity.this.showSoftKeyboard(txt);
            }
        },50);

        //
        // setup button handlers
        //
        Button backBtn = (Button)findViewById(R.id.keyboard_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardActivity.this._handleBack();
            }
        });

        Button hideBtn = (Button)findViewById(R.id.keyboard_hide_btn);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardActivity.this._handleHide();
            }
        });

        showAppIcon();

    }

    /**
     * show the keyboard.
     */
    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * End this activity
     */
    private void _handleBack()
    {
        finish();
    }


    /**
     * Hide the soft keyboard
     */
    private void _handleHide()
    {

        EditText txt = (EditText)findViewById(R.id.keyboard_top_text);
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            _handleBack();
        }
        return super.onOptionsItemSelected(item);
    }
}
