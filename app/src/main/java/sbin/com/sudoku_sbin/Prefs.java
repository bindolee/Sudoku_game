package sbin.com.sudoku_sbin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;


/**
 * Created by sbin on 10/18/2016.
 */

public class Prefs extends PreferenceActivity
        implements AppCompatCallback {

    private AppCompatDelegate delegate;

    // Option names and default values
    private static final String KEY_MUSIC = "music";
    private static final boolean OPT_MUSIC_DEF = true;
    private static final String KEY_HINTS = "hints";
    private static final boolean OPT_HINTS_DEF = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(true);
        delegate.getSupportActionBar().setTitle(R.string.settings_title);
        //delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/





        /*AppCompatActivity appCompatActivity = getCallingActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }

    public static boolean getMusic(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_MUSIC, OPT_MUSIC_DEF);
    }

    public static boolean getHints(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_HINTS, OPT_HINTS_DEF);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
