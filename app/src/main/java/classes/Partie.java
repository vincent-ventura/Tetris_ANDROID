package classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Vincent on 28/06/2017.
 */
public class Partie {
    private String playerName;
    private int score;
    private TextView scoreView;
    private int level;
    private TextView levelView;
    private int timeBetweenTwoPieces; // in ms
    private int totalLines;
    private boolean endGame;
    private boolean pause;

    public Partie(TextView scoreView, TextView levelView, String playerName) {
        this.playerName = playerName;
        this.level = 1;
        this.levelView = levelView;
        this.timeBetweenTwoPieces = 500;
        this.totalLines = 0;
        this.score = 0;
        this.scoreView = scoreView;
        this.endGame = false;
        this.pause = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimeBetweenTwoPieces() {
        return timeBetweenTwoPieces;
    }

    public void setTimeBetweenTwoPieces(int timeBetweenTwoPieces) {
        this.timeBetweenTwoPieces = timeBetweenTwoPieces;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public void addCompletedLines(int lines) {
        for (int i=0; i<lines; i++){
            this.totalLines++;
            if (String.valueOf(totalLines).endsWith("0"))
                // new level
                this.level = this.level + 1;
        }
        this.levelView.setText(String.valueOf(this.level));
        this.timeBetweenTwoPieces = 500 - this.level * 10;
    }

    public void updateScore(int nbCompletedLines) {
        switch ( nbCompletedLines ) {
            case 1:
                this.score+=40; //* level;
                this.addCompletedLines(1);
                break;
            case 2:
                this.score+=100;
                this.addCompletedLines(2);
                break;
            case 3:
                this.score+=300;
                this.addCompletedLines(3);
                break;
            case 4: // 4 lines = tetris
                this.score+=1200;
                this.addCompletedLines(4);
                break;
            default: // game start
                System.out.println("ANORMAL BEHAVIOR");
        }
        this.scoreView.setText(String.valueOf(this.score));
    }

    public void init(int[][] gameGrid, ArrayList<ImageView> imageList, Context context) {
        // init the grid images views
        for (int i = 0; i < 200; i++) {
            ImageView iv = new ImageView(context);
            iv.setBackgroundColor(Color.BLACK);
            imageList.add(iv);
        }
        // init the grid values
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++)
                gameGrid[x][y] = 0;

        // update level and score view for the first time
        this.levelView.setText("1");
        this.scoreView.setText("0");
    }

    public void saveHighScore(SharedPreferences highScores) {
        SharedPreferences.Editor editor = highScores.edit();
        int lim = 5; // Classement des 5 meilleurs scores -> peut etre changé
        int classement = 0; // classement score actuel
        int i = 0; // représente le classement du meilleur score en train d'etre traité

        while (classement == 0 && i <= lim) { // tant que classement pas trouvé ou que tous les high scores pas parcourus
            i++;
            int highScore = Integer.valueOf( highScores.getString("score"+String.valueOf(i), "0") );
            if (this.score > highScore)
                classement = i;
        }

        if (classement != 0) { // new high score
            int nbHighScores = highScores.getAll().size()/2;
            if ( nbHighScores < lim)
                nbHighScores++;

            for (int n=nbHighScores; n>=n+1; n--) {
                    String scoreToMove = highScores.getString("score"+String.valueOf(n-1), "0");
                    String playerToMove = highScores.getString("player" + String.valueOf(n-1), null);
                    editor.putString("score"+String.valueOf(n), scoreToMove);
                    editor.putString("player"+String.valueOf(n), playerToMove);
            }
            editor.putString("score"+String.valueOf(i), String.valueOf(this.score));
            editor.putString("player"+String.valueOf(i), String.valueOf(this.playerName));
        }

        editor.apply();
    }

}
