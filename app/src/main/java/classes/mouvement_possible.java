package classes;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vincent on 15/05/2017.
 */
public interface mouvement_possible {
    public boolean rotate_possible(int[][] game, ArrayList<Integer> colorList);
    public boolean left_possible(int[][] game);
    public boolean right_possible(int[][] game);
    public boolean down_possible(int[][] game);
}
