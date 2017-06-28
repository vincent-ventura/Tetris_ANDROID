package com.vincent.tetris.activities;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.tetris.R;

import java.util.ArrayList;
import java.util.Random;

import classes.ImageViewAdapter;
import classes.Piece;
import classes.Piece_I;
import classes.Piece_J;
import classes.Piece_L;
import classes.Piece_O;
import classes.Piece_S;
import classes.Piece_T;
import classes.Piece_Z;

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
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initGame();

        gameGridView = (GridView) findViewById(R.id.game_grid);
        myImageAdapter = new ImageViewAdapter(this, gridImageList, gameGridView);
        gameGridView.setAdapter(myImageAdapter);

        // set score to zero
        score = (TextView) findViewById(R.id.score_value);
        score.setText("0");

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
                    searchAndRemoveCompletedLines();
                    pieces.add(randomPiece());
                    pieces.get(pieces.size()-1).placer(gameGrid, gridImageList);
                    setButtonsEnabled(true);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);

    }

    private void initGame() {
        // init the grid images views
        for (int i=0; i<200; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundColor(Color.BLACK);
            gridImageList.add(iv);
        }
        // init the grid values
        for (int x=0; x<10; x++)
            for (int y=0; y<10; y++)
                gameGrid[x][y] = 0;
    }

    private Piece randomPiece() {
        Piece randomPiece = null;
        Random random = new Random();
        int num = random.nextInt(6);
        switch (num) {
            case 0:
                randomPiece = new Piece_I();
                break;
            case 1:
                randomPiece = new Piece_J();
                break;
            case 2:
                randomPiece = new Piece_L();
                break;
            case 3:
                randomPiece = new Piece_O();
                break;
            case 4:
                randomPiece = new Piece_S();
                break;
            case 5:
                randomPiece = new Piece_T();
                break;
            case 6:
                randomPiece = new Piece_Z();
                break;
        }
        return randomPiece;
    }

    private void setButtonsEnabled(Boolean enable) {
        rightButton.setEnabled(enable);
        downButton.setEnabled(enable);
        leftButton.setEnabled(enable);
        downButton.setEnabled(enable);
        rotateButton.setEnabled(enable);
    }

    private void searchAndRemoveCompletedLines() {
        int completedLineNb=0;
        for (int line=0; line<20; line++) {
            boolean completedLine=true;
            int column=0;

            while( column<10 && true==completedLine ) {
                if (gameGrid[column][line] == 0)
                    completedLine = false;
                column++;
            }

            if (completedLine) {
                completedLineNb++;
                for (int i = 0; i < 10; i++) {
                    gameGrid[i][line] = 0;
                    gridImageList.get(line * 10 + i).setBackgroundColor(Color.BLACK);
                }
                // .down on all pieces that are "higher" than the removed line
                for (int posLine=line-1; posLine>=0; posLine--) {
                    for (int ind = 0; ind < pieces.size(); ind++) {
                        if (pieces.get(ind).getPos_j() == posLine) {
                            pieces.get(ind).down(gameGrid, gridImageList, false);
                        }
                    }
                }

            }
        }
        if (completedLineNb > 0) {
            updateScore( completedLineNb, 0 );
        }
        gameGridView.setAdapter(myImageAdapter);
    }

    private void updateScore( int nbCompletedLines, int level ) {
        int score = Integer.valueOf((String) this.score.getText());
        switch ( nbCompletedLines ) {
            case 1:
                score+=40; //* level;
                break;
            case 2:
                score+=100;
                break;
            case 3:
                score+=300;
                break;
            default: // 4 lines = tetris
                score+=1200;
        }
        this.score.setText( String.valueOf(score) );
    }


}
