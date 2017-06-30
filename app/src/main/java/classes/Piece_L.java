package classes;

import android.graphics.Color;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public class Piece_L extends Piece {
    static final int HAUTEUR_DEPART = 2;
    static final int LARGEUR_DEPART = 3;
    static final int[][] MATRICE_0 = { {0,1}, {0,1}, {1,1} };
    static final int[][] MATRICE_1 = { {1,1,1}, {0,0,1} };
    static final int[][] MATRICE_2 = { {1,1}, {1,0}, {1,0} };
    static final int[][] MATRICE_3 = { {1,0,0}, {1,1,1} };

    public Piece_L() {
        super(HAUTEUR_DEPART, LARGEUR_DEPART, MATRICE_0, Color.rgb(49, 140, 231));
    }

    public Pair<int[][], Integer> getNextMatrixRotation() {
        int[][] matrice = null;
        Integer matriceNum = null;

        switch (this.matriceNum) {
            case 0:
                matrice = MATRICE_1;
                matriceNum = 1;
                break;
            case 1:
                matrice = MATRICE_2;
                matriceNum = 2;
                break;
            case 2:
                matrice = MATRICE_3;
                matriceNum = 3;
                break;
            case 3:
                matrice = MATRICE_0;
                matriceNum = 0;
                break;
        }

        return new Pair(matrice, matriceNum);
    }

}
