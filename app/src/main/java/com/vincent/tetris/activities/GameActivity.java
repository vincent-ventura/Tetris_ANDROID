package com.vincent.tetris.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.vincent.tetris.R;
import java.util.ArrayList;
import classes.ImageViewAdapter;
import classes.Partie;
import classes.Piece;

public class GameActivity extends AppCompatActivity {
    private int [][] gameGrid = new int[10][20];
    private ArrayList<Integer> gridColorList = new ArrayList<>();
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private GridView gameGridView;
    private ImageViewAdapter myImageAdapter;
    private Handler handler = new Handler();
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton downButton;
    private ImageButton rotateButton;
    private ImageButton pauseButton;
    private Partie partie;
    public static final String HIGH_SCORES_PREFS = "HighScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView scoreView = (TextView) findViewById(R.id.score_value);
        TextView levelView = (TextView) findViewById(R.id.level_value);

        Bundle extras = getIntent().getExtras();
        partie = new Partie(scoreView, levelView, extras.getString("playerName"), extras.getInt("level"));
        partie.init(gameGrid, gridColorList, this);

        gameGridView = (GridView) findViewById(R.id.game_grid);
        myImageAdapter = new ImageViewAdapter(this, gridColorList, gameGridView);
        gameGridView.setAdapter(myImageAdapter);

        // defines the game loop
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                // refresh the gridview adapter
                gameGridView.setAdapter(myImageAdapter);
                if ( pieces.isEmpty() || !pieces.get(pieces.size()-1).down(gameGrid, gridColorList, true) ) {
                    setButtonsEnabled(false); // to avoid moving a piece before it was set on game
                    searchAndRemoveCompletedLines(gameGrid, gridColorList, pieces);
                    pieces.add(Piece.randomPiece());
                    pieces.get(pieces.size()-1).placer(gameGrid, gridColorList, partie);
                    if ( partie.isEndGame() ) {
                        partie.setPause(true);
                        showEndGame();
                    }
                    setButtonsEnabled(true);
                }
                if ( !partie.isPause() ) {
                    handler.postDelayed(this, partie.getTimeBetweenTwoPieces());
                }
            }
        };

        // set listenner to game buttons
        leftButton = (ImageButton) findViewById(R.id.left_button);
        rightButton = (ImageButton) findViewById(R.id.right_button);
        downButton = (ImageButton) findViewById(R.id.down_button);
        rotateButton = (ImageButton) findViewById(R.id.rotate_button);
        pauseButton = (ImageButton) findViewById(R.id.pause_button);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).left(gameGrid, gridColorList);
                gameGridView.setAdapter(myImageAdapter);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).right(gameGrid, gridColorList);
               gameGridView.setAdapter(myImageAdapter);
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).down(gameGrid, gridColorList, true);
               gameGridView.setAdapter(myImageAdapter);
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).rotate(gameGrid, gridColorList);
               gameGridView.setAdapter(myImageAdapter);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( partie.isPause() ) { // play button pressed
                    partie.setPause(false);
                    handler.postDelayed(r, partie.getTimeBetweenTwoPieces());
                    pauseButton.setImageResource(R.drawable.ic_pause_circle_fill_24dp);
                    setButtonsEnabled(true);
                } else { // pause button pressed
                    partie.setPause(true);
                    pauseButton.setImageResource(R.drawable.ic_play_circle_fill_24dp);
                    setButtonsEnabled(false);
                }
            }
        });

        handler.postDelayed(r, partie.getTimeBetweenTwoPieces());

    }

    private void setButtonsEnabled(Boolean enable) {
        rightButton.setEnabled(enable);
        downButton.setEnabled(enable);
        leftButton.setEnabled(enable);
        downButton.setEnabled(enable);
        rotateButton.setEnabled(enable);
    }

    public boolean searchAndRemoveCompletedLine(int[][] gameGrid, ArrayList<Integer> colorList, ArrayList<Piece> pieces) {
        int line = 19;
        boolean completedLineFound=false;

        while (line>=0 && !completedLineFound) {
            boolean completedLine=true;
            int column=0;
            while( column<10 && completedLine ) {
                if (gameGrid[column][line] == 0)
                    completedLine = false;
                column++;
            }

            if (completedLine) {
                completedLineFound=true;
                for (int i = 0; i < 10; i++) {
                    gameGrid[i][line] = 0;
                    colorList.set(line * 10 + i, Color.BLACK);
                }
                // .down on all pieces that are "higher" than the removed line (line)
                for (int posLine=line-1; posLine>=0; posLine--) {
                    for (int ind = 0; ind < pieces.size(); ind++) {
                        if (pieces.get(ind).getPos_j() == posLine) {
                            pieces.get(ind).down(gameGrid, colorList, false);
                        }
                    }
                }
            }
            line--;
        }

        return completedLineFound;
    }

    public void searchAndRemoveCompletedLines(int[][] gameGrid, ArrayList<Integer> colorList, ArrayList<Piece> pieces) {
        int completedLineNb=0;

        while (searchAndRemoveCompletedLine(gameGrid, colorList, pieces)) {
            completedLineNb++;
        }

        if (completedLineNb > 0) {
            partie.updateScore(completedLineNb);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences highScores = getSharedPreferences(HIGH_SCORES_PREFS, MODE_PRIVATE);
        partie.saveHighScore(highScores);
    }

    @Override
    public void onBackPressed() {
        if ( !partie.isPause() )
            pauseButton.callOnClick();
        confirmation();
    }

    public void confirmation() {
        new AlertDialog.Builder(this).setCancelable(false)
            .setTitle("Quitter")
            .setMessage("Voulez vous quitter la partie ?")
            .setPositiveButton("Oui",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
            .setNegativeButton("Non",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // AlertDialog.cancel();
                    }
                })
            .create()
            .show();
    }

    public void showEndGame() {
        new AlertDialog.Builder(this).setCancelable(false)
            .setTitle("Fin de partie")
            .setMessage(String.valueOf(partie.getScore()) + " points!")
            .setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                .create()
                .show();
    }

}
