package com.indysoft.amazingtetris;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by Mircea on 16.05.2015.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int backgroundColor = Color.parseColor("#ff72abff");
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(backgroundColor);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true); // !?!?

    }
}
