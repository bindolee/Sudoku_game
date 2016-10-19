package sbin.com.sudoku_sbin;

import android.os.Bundle;
import android.preference.PreferenceActivity;
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
