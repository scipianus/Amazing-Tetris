package com.indysoft.amazingtetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (HelpSaveSettings.saveSettingsApp == 0) {
            HelpSaveSettings.saveSettingsApp = 1;
            prefs.edit().putString("difficulty_preference", "Normal").apply();
            prefs.edit().putString("num_rows_preference", "20").apply();
            prefs.edit().putString("num_columns_preference", "10").apply();
            prefs.edit().putString("speed_preference", "Normal").apply();
        }


        // Restore the player's name
        SharedPreferences settings = getSharedPreferences("Preferences", 0);

        playerName = settings.getString("playerName", "");
        //PreferenceManager.setDefaultValues(this, R.xml.preferences, true); // !?!?

        // Set the newGameButton
        final Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (playerName.isEmpty()) {
                            Toast.makeText(newGameButton.getContext(), "Please insert a name!", Toast.LENGTH_SHORT).show();
                        } else {
                            MainActivity.this.startActivity(new Intent(MainActivity.this, GameActivity.class));
                        }
                    }
                });

        // Set the highScoresButton
        Button highScoresButton = (Button) findViewById(R.id.high_scores_button);
        highScoresButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        HelpSaveSettings.saveSettingsHighscores = 0;
                        MainActivity.this.startActivity(new Intent(MainActivity.this, HighScoresActivity.class));
                    }
                });

        // Set the aboutButton
        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    }
                });

        // Set the settingsButton
        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                });

        // Set the nameEditText
        EditText editText = (EditText) findViewById(R.id.nameEditText);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                playerName = s.toString(); // update playerName when nameEditText changes
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        editText.setText(playerName);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this.getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the player's name between sessions
        // Toast.makeText(this.getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
        SharedPreferences settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("playerName", playerName);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}


