package classes;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public interface mouvement {
    void rotate(int[][] game, ArrayList<Integer> colorList);
    void left(int[][] game, ArrayList<Integer> colorList);
    void right(int[][] game, ArrayList<Integer> colorList);
    boolean down(int[][] game, ArrayList<Integer> colorList, boolean checkIfPossible);
}
