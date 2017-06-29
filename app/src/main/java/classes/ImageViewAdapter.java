package classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.vincent.tetris.R;

import java.util.ArrayList;

/**
 * Created by Vincent on 19/05/2017.
 */
public class ImageViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> colorList; // TODO : Replace by ImageView[][] tab
    private GridView gridView;

    public ImageViewAdapter(Context c, ArrayList<Integer> colorList, GridView gridView)
    {
        this.mContext = c;
        this.colorList = colorList;
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        return colorList != null ? colorList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return colorList != null ? colorList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return colorList != null ? position : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = null;

        if (convertView == null) { // if it's a new iv
            iv = new ImageView(mContext);
            iv.setBackgroundColor( this.colorList.get(position) );
            iv.setLayoutParams(new GridView.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth()));
        } else {
            iv = (ImageView) convertView;
        }

        return iv;
    }

}
