package classes;

import android.graphics.Color;
import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public class Piece_O extends Piece {
    private static final int HAUTEUR_DEPART = 2;
    private static final int LARGEUR_DEPART = 2;
    private static final int[][] MATRICE_0 = { {1, 1}, {1, 1} };


    public Piece_O() {
        super(HAUTEUR_DEPART, LARGEUR_DEPART, MATRICE_0, Color.rgb(255, 215, 0));
    }

    public Pair<int[][], Integer> getNextMatrixRotation() {
        return new Pair(this.matrice, this.matriceNum);
    }
}
