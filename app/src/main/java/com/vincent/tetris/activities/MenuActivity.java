package com.vincent.tetris.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vincent.tetris.R;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final EditText playerName = (EditText) findViewById(R.id.player_name);
        final Spinner spinnerLevelChoice  = (Spinner) findViewById(R.id.levelChoice);
        List<Integer> levels = new ArrayList<Integer>();
        levels.add(1);
        levels.add(5);
        levels.add(10);
        levels.add(20);

        ArrayAdapter<Integer> adapterLevelChoice = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, levels);
        adapterLevelChoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevelChoice.setAdapter(adapterLevelChoice);

        Button newGameBtn = (Button) findViewById(R.id.new_game);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                String pName = TextUtils.isEmpty(playerName.getText()) ? "Joueur1" : playerName.getText().toString();
                intent.putExtra("playerName", pName);
                intent.putExtra("level", (Integer) spinnerLevelChoice.getSelectedItem());
                startActivity(intent);
            }
        });

        Button highScore = (Button) findViewById(R.id.high_scores);
        highScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HighScoresActivity.class);
                startActivity(intent);
            }
        });
    }
}
