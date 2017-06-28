package classes;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public abstract class Piece implements mouvement, mouvement_possible {
    protected int hauteur;
    protected int largeur;
    protected int[][] matrice;
    protected Integer matriceNum;
    protected int pos_i;
    protected int pos_j;
    protected int color;

    public Piece () {}

    public Piece(int hauteur, int largeur, int pos_i, int pos_j, int[][] matrice, int color) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.pos_i = pos_i;
        this.pos_j = pos_j;
        this.matrice = matrice;
        this.matriceNum = 0;
        this.color = color;
    }

    public Piece(int hauteur, int largeur, int[][] matrice, int color) {
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.pos_i = (10/2) - (largeur/2);
        this.pos_j = 0;
        this.matrice = matrice;
        this.matriceNum = 0;
        this.color = color;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public int[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(int[][] matrice) {
        this.matrice = matrice;
    }

    public int getPos_i() {
        return pos_i;
    }

    public void setPos_i(int pos_i) {
        this.pos_i = pos_i;
    }

    public int getPos_j() {
        return pos_j;
    }

    public void setPos_j(int pos_j) {
        this.pos_j = pos_j;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void placer(int[][] game, ArrayList<ImageView> imageList) {
        for (int i=this.pos_i, x=0; x<this.largeur; i++,x++) {
            for (int j=this.pos_j, y=0; y<this.hauteur; j++, y++) {
                if (i >= 0 && i <= 9 && j >= 0 && j <= 19 && this.matrice[x][y] == 1) {
                    game[i][j] = 1;
                    imageList.get(i + 10 * j).setBackgroundColor(this.color);
                }
            }
        }
    }

    public void effacer(int[][] game, ArrayList<ImageView> imageList) {
        for (int i=this.pos_i, x=0; x<this.largeur; i++,x++) {
            for (int j=this.pos_j, y=0; y<this.hauteur; j++, y++) {
                if ( i>=0 && i<=9 && j>=0 && j<=19 && game[i][j] == 1 && this.matrice[x][y] == 1 ) {
                    imageList.get(i + 10 * j).setBackgroundColor(Color.BLACK);
                    game[i][j] = 0;
                }
            }
        }
    }

    public void rotate(int [][] game, ArrayList<ImageView> imageList) {
        this.effacer(game, imageList);

        if( this.rotate_possible(game, imageList) ) {
            this.effacer(game,imageList);

            int backup_hauteur = this.hauteur;
            this.hauteur = this.largeur;
            this.largeur = backup_hauteur;

            Pair<int [][], Integer> p = this.getNextMatrixRotation();
            this.matrice = p.first;
            this.matriceNum = p.second;

            int right_depassement = this.pos_i + (this.largeur-1) - 9;
            if (right_depassement > 0 )
                this.pos_i -= right_depassement;

        }
        this.placer(game, imageList);
    };

    public boolean rotate_possible(int[][] game, ArrayList<ImageView> imageList) {
        boolean possible = true;
        // simulate piece rotation
        int hauteur = this.largeur;
        int largeur = this.hauteur;
        Pair<int [][], Integer> p = this.getNextMatrixRotation();
        int right_depassement = this.pos_i + (largeur-1) - 9;
        int new_pos_i = this.pos_i;

        if (right_depassement > 0 )
            new_pos_i = this.pos_i - right_depassement;

        for (int i=new_pos_i, x=0; x<largeur; i++,x++) {
            for (int j = this.pos_j, y = 0; y < hauteur; j++, y++) {
                if (p.first[x][y] == 1 && game[i][j] == 1) {
                    //ColorDrawable drawable = (ColorDrawable) imageList.get(j * 10 + i).getBackground(); // get color from imageList array
                    //if (drawable.getColor() != Color.BLACK)
                    possible = false;
                }
            }
        }
        return possible;
    };

    public void left(int[][] game, ArrayList<ImageView> imageList) {
        this.effacer(game, imageList);
        if( this.left_possible(game) )
            this.pos_i--;
        this.placer(game, imageList);
    }

    public boolean left_possible(int[][] game) {
        // check if the first matrice column can get -1 in x axis
        if (this.pos_i < 1) return false;

        boolean possible = true;
        int p_i = pos_i - 1;
        for (int i=p_i, x=0; x<largeur; i++,x++) {
            for (int j = this.pos_j, y = 0; y<hauteur; j++, y++) {
                if ( (i>9) || (this.matrice[x][y] == 1 && game[i][j] == 1) ) {
                    possible = false;
                }
            }
        }
        return possible;
    }


    public void right(int[][] game, ArrayList<ImageView> imageList) {
        this.effacer(game,imageList);
        if (this.right_possible(game))
            this.pos_i++;
        this.placer(game, imageList);
    }

    public boolean right_possible(int[][] game){
        // check if the last matrice column can get +1 in x axis
        if (this.pos_i + 1 + this.largeur-1 > 9) return false;

        boolean possible = true;
        int p_i = pos_i + 1;
        for (int i=p_i, x=0; x<largeur; i++,x++) {
            for (int j = this.pos_j, y = 0; y<hauteur; j++, y++) {
                if ( (i>9) || (this.matrice[x][y] == 1 && game[i][j] == 1) ) {
                    possible = false;
                }
            }
        }
        return possible;
    }

    public boolean down(int[][] game, ArrayList<ImageView> imageList, boolean checkIfPossible) {
        this.effacer(game, imageList);

        boolean down_possible = !checkIfPossible || this.down_possible(game);
        if( down_possible )
            this.pos_j++;

        this.placer(game, imageList);
        return down_possible;
    }

    public boolean down_possible(int[][] game){
        // check if the last matrice line can get +1 in y axis
        if (this.pos_j + 1 + this.hauteur-1 > 19) return false;

        boolean possible = true;
        int p_j = pos_j + 1;
        for (int i=this.pos_i, x=0; x<largeur; i++,x++) {
            for (int j = p_j, y = 0; y<hauteur; j++, y++) {
                if ( (j>19) || (this.matrice[x][y] == 1 && game[i][j] == 1) ) {
                    possible = false;
                }
            }
        }
        return possible;
    }

    public abstract Pair<int[][], Integer> getNextMatrixRotation();

}
