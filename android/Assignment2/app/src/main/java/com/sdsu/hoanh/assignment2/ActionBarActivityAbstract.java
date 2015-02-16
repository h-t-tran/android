package com.sdsu.hoanh.assignment2;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * common class containing some reusable methods
 */
public abstract class ActionBarActivityAbstract extends ActionBarActivity {
    /**
     * Enable the app icon.  we use getSupportActionBar() for backward compatibility
     */
    protected void showAppIcon() {

        ActionBar bar = this.getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setLogo(R.drawable.ic_launcher);
        bar.setDisplayUseLogoEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);

    }

}
