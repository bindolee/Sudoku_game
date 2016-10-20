package sbin.com.sudoku_sbin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;


/**
 * Created by sbin on 10/18/2016.
 */

public class Prefs extends PreferenceActivity
{
    private AppCompatDelegate mDelegate;

    // Option names and default values
    private static final String KEY_MUSIC = "music";
    private static final boolean OPT_MUSIC_DEF = true;
    private static final String KEY_HINTS = "hints";
    private static final boolean OPT_HINTS_DEF = true;
    private static final String LOG_TAG = "Prefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        //Need to change AndroidManifest.xml to set this has actionbar Theme.AppCompat....Actionbar
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        if (getDelegate() == null) {
            Log.i(LOG_TAG, "delegate: "+ mDelegate);
            return;
        }

        ActionBar actionBar = getDelegate().getSupportActionBar();
        if (actionBar == null) throw new AssertionError("Actionbar is null");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.settings_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static boolean getMusic(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_MUSIC, OPT_MUSIC_DEF);
    }

    public static boolean getHints(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_HINTS, OPT_HINTS_DEF);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    //Listener for setDisplayHomeAsUpEnabled(true) -- android.R.id.home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
