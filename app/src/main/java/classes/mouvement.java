package classes;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public interface mouvement {
    void rotate(int[][] game, ArrayList<ImageView> imageList);
    void left(int[][] game, ArrayList<ImageView> imageList);
    void right(int[][] game, ArrayList<ImageView> imageList);
    boolean down(int[][] game, ArrayList<ImageView> imageList, boolean checkIfPossible);
}
