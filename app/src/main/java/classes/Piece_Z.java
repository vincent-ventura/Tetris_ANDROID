package classes;

import android.graphics.Color;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public class Piece_Z extends Piece {
    static final int HAUTEUR_DEPART = 2;
    static final int LARGEUR_DEPART = 3;
    static final int[][] MATRICE_0 = { {1,0}, {1,1}, {0,1} };
    static final int[][] MATRICE_1 = { {0,1,1}, {1,1,0} };

    public Piece_Z() {
        super(HAUTEUR_DEPART, LARGEUR_DEPART, MATRICE_0, Color.RED);
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
                matrice = MATRICE_0;
                matriceNum = 0;
                break;
        }

        return new Pair(matrice, matriceNum);
    }

}
