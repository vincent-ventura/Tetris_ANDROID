package com.vincent.tetris.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vincent.tetris.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final EditText playerName = (EditText) findViewById(R.id.player_name);

        Button newGameBtn = (Button) findViewById(R.id.new_game);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                String pName = TextUtils.isEmpty(playerName.getText()) ? "Joueur1" : playerName.getText().toString();
                intent.putExtra("playerName", pName);
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
