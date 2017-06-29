package com.vincent.tetris.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.tetris.R;

public class HighScoresActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_scores);

        LinearLayout scoresLayout = (LinearLayout) findViewById(R.id.scoresLayout);
        SharedPreferences highScores = getSharedPreferences(GameActivity.HIGH_SCORES_PREFS, MODE_PRIVATE);

        for (int i=1; i<=5; i++) {
            String player = highScores.getString("player"+String.valueOf(i), null);
            String score = highScores.getString("score" + String.valueOf(i), "0");

            if ( player != null ) {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv = new TextView(this);
                String text = String.valueOf(i) + ". " + player + " : " + score;
                tv.setLayoutParams(lparams);
                tv.setTextSize(25);
                tv.setText(text);

                scoresLayout.addView(tv);
            }
        }



    }
}
