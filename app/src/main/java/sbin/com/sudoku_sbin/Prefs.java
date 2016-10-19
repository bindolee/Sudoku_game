package sbin.com.sudoku_sbin;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Created by sbin on 10/18/2016.
 */

public class Prefs extends android.preference.PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
