package com.indysoft.amazingtetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class HighScoresActivity extends Activity {

    ListView list;

    void PopulateListView()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<HashMap<String, String>> mList= new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mMap;

        mMap = new HashMap<String, String>();
        mMap.put("ID", "1");
        mMap.put("Name", "Mircea");
        mMap.put("Score", "123");
        mList.add(mMap);

        mMap = new HashMap<String, String>();
        mMap.put("ID", "2");
        mMap.put("Name", "Miguel");
        mMap.put("Score", "10011");
        mList.add(mMap);

        mMap = new HashMap<String, String>();
        mMap.put("ID", "3");
        mMap.put("Name", "Murtaza");
        mMap.put("Score", "0");
        mMap.put("Score", "1010");
        mList.add(mMap);

        mMap = new HashMap<String, String>();
        mMap.put("ID", "4");
        mMap.put("Name", "Vled");
        mMap.put("Score", "69");
        mList.add(mMap);

        mMap = new HashMap<String, String>();
        mMap.put("ID", "5");
        mMap.put("Name", "Cristian-Teodor Tudor");
        mMap.put("Score", "0");
        mList.add(mMap);

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mList, R.layout.rowtype_highscores, new String[] {"ID", "Name", "Score"}, new int[] {R.id.ID_CELL, R.id.NAME_CELL, R.id.SCORE_CELL});
        list.setAdapter(mSimpleAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        //this.option

        list = (ListView) findViewById(R.id.listView_highscores);
        Button setPropertiesButton = (Button)findViewById(R.id.set_properties_highscores_button);
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
        Toast.makeText(this.getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PopulateListView();
        Toast.makeText(this.getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PopulateListView();
        Toast.makeText(this.getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
    }
}
