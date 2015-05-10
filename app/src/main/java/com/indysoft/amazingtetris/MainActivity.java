package com.indysoft.amazingtetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    public String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore the player's name
        SharedPreferences settings = getSharedPreferences("Preferences", 0);
        playerName = settings.getString("playerName", "");

        // Set the newGameButton
        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, GameActivity.class));
                    }
                });

        // Set the highScoresButton
        Button highScoresButton = (Button) findViewById(R.id.high_scores_button);
        highScoresButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
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
    protected void onStop() {
        super.onStop();

        // Save the player's name between sessions
        SharedPreferences settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("playerName", playerName);
        editor.commit();
    }
}
