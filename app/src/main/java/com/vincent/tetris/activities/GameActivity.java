package com.vincent.tetris.activities;

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
    private ArrayList<ImageView> gridImageList = new ArrayList<>();
    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private GridView gameGridView;
    private ImageViewAdapter myImageAdapter;
    private Handler handler = new Handler();
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton downButton;
    private ImageButton rotateButton;
    private Partie partie;
    private static final String HIGH_SCORES = "HighScores";
    private Boolean finPartie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView scoreView = (TextView) findViewById(R.id.score_value);
        TextView levelView = (TextView) findViewById(R.id.level_value);

        Bundle extras = getIntent().getExtras();
        partie = new Partie(scoreView, levelView, extras.getString("playerName"));
        partie.init(gameGrid, gridImageList, this);
        finPartie = false;

        gameGridView = (GridView) findViewById(R.id.game_grid);
        myImageAdapter = new ImageViewAdapter(this, gridImageList, gameGridView);
        gameGridView.setAdapter(myImageAdapter);


        // set listenner to game buttons
        leftButton = (ImageButton) findViewById(R.id.left_button);
        rightButton = (ImageButton) findViewById(R.id.right_button);
        downButton = (ImageButton) findViewById(R.id.down_button);
        rotateButton = (ImageButton) findViewById(R.id.rotate_button);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).left(gameGrid, gridImageList);
                gameGridView.setAdapter(myImageAdapter);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).right(gameGrid, gridImageList);
                gameGridView.setAdapter(myImageAdapter);
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).down(gameGrid, gridImageList, true);
                gameGridView.setAdapter(myImageAdapter);
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieces.get(pieces.size() - 1).rotate(gameGrid, gridImageList);
                gameGridView.setAdapter(myImageAdapter);
            }
        });

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                // refresh the gridview adapter
                gameGridView.setAdapter(myImageAdapter);
                if ( pieces.isEmpty() || !pieces.get(pieces.size()-1).down(gameGrid, gridImageList, true) ) {
                    setButtonsEnabled(false); // to avoid moving a piece before it was set on game
                    searchAndRemoveCompletedLines(gameGrid, gridImageList, pieces);
                    pieces.add(Piece.randomPiece());
                    pieces.get(pieces.size()-1).placer(gameGrid, gridImageList, finPartie);
                    if (finPartie) {
                        // TODO : Confirmation
                        /* Fin partie :
                            OK : Menu Principal
                        */
                    }
                    setButtonsEnabled(true);
                }
                handler.postDelayed(this, partie.getTimeBetweenTwoPieces());
            }
        };
        handler.postDelayed(r, partie.getTimeBetweenTwoPieces());

    }

    private void setButtonsEnabled(Boolean enable) {
        rightButton.setEnabled(enable);
        downButton.setEnabled(enable);
        leftButton.setEnabled(enable);
        downButton.setEnabled(enable);
        rotateButton.setEnabled(enable);
    }

    public void searchAndRemoveCompletedLines(int[][] gameGrid, ArrayList<ImageView> imageList, ArrayList<Piece> pieces) {
        int completedLineNb=0;
        for (int line=0; line<20; line++) {
            boolean completedLine=true;
            int column=0;
            while( column<10 && completedLine ) {
                if (gameGrid[column][line] == 0)
                    completedLine = false;
                column++;
            }

            if (completedLine) {
                completedLineNb++;
                for (int i = 0; i < 10; i++) {
                    gameGrid[i][line] = 0;
                    imageList.get(line * 10 + i).setBackgroundColor(Color.BLACK);
                }
                // .down on all pieces that are "higher" than the removed line (line)
                for (int posLine=line-1; posLine>=0; posLine--) {
                    for (int ind = 0; ind < pieces.size(); ind++) {
                        if (pieces.get(ind).getPos_j() == posLine) {
                            pieces.get(ind).down(gameGrid, imageList, false);
                        }
                    }
                }

            }
        }
        if (completedLineNb > 0) {
            partie.updateScore(completedLineNb);
        }
        //gameGridView.setAdapter(myImageAdapter);
    }

    @Override
    protected void onStop(){
        // TODO : CONFIRMATION
        super.onStop();

        SharedPreferences highScores = getSharedPreferences(HIGH_SCORES, MODE_PRIVATE);
        partie.saveHighScore(highScores);
    }

}
