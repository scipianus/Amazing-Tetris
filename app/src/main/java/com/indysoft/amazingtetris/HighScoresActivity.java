package com.indysoft.amazingtetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class HighScoresActivity extends Activity {

    ListView list;

    void PopulateListView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mMap;

        String[] projection = {
                HighScoresDb.KEY_NAME,
                HighScoresDb.KEY_SCORE};

        String selection = HighScoresDb.KEY_DIFFICULTY + "=?" + " and "
                + HighScoresDb.KEY_NUMROWS + "=?" + " and "
                + HighScoresDb.KEY_NUMCOLUMNS + "=?" + " and "
                + HighScoresDb.KEY_SPEED + " =?";

        String[] selectionArgs = {
                prefs.getString("difficulty_preference_highscores", "Normal"),
                prefs.getString("num_rows_preference_highscores", "20"),
                prefs.getString("num_columns_preference_highscores", "10"),
                prefs.getString("speed_preference_highscores", "Normal")
        };

        String sortOrder = HighScoresDb.KEY_SCORE + " DESC";

        Cursor cursor = getContentResolver().query(HighScoresContentProvider.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor == null || cursor.getCount() == 0) {
            mMap = new HashMap<String, String>();
            mMap.put("ID", "1");
            mMap.put("Name", "N/A");
            mMap.put("Score", "N/A");
            mList.add(mMap);
        } else {
            int id = 0;
            cursor.moveToFirst();
            do {
                id++;
                mMap = new HashMap<String, String>();
                mMap.put("ID", Integer.toString(id));
                mMap.put("Name", cursor.getString(cursor.getColumnIndexOrThrow(HighScoresDb.KEY_NAME)));
                mMap.put("Score", Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(HighScoresDb.KEY_SCORE))));
                mList.add(mMap);
            }
            while (cursor.moveToNext());
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mList, R.layout.rowtype_highscores, new String[]{"ID", "Name", "Score"}, new int[]{R.id.ID_CELL, R.id.NAME_CELL, R.id.SCORE_CELL});
        list.setAdapter(mSimpleAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        //this.option

        list = (ListView) findViewById(R.id.listView_highscores);
        Button setPropertiesButton = (Button) findViewById(R.id.set_properties_highscores_button);
        setPropertiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighScoresActivity.this.startActivity(new Intent(HighScoresActivity.this, SettingsHighscoresActivity.class));
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (HelpSaveSettings.saveSettingsHighscores == 0) {
            HelpSaveSettings.saveSettingsHighscores = 1;
            prefs.edit().putString("difficulty_preference_highscores", "Normal").apply();
            prefs.edit().putString("num_rows_preference_highscores", "20").apply();
            prefs.edit().putString("num_columns_preference_highscores", "10").apply();
            prefs.edit().putString("speed_preference_highscores", "Normal").apply();
        }
        PopulateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PopulateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PopulateListView();
    }
}
