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
    private ArrayList<ImageView> imageList; // TODO : Replace by ImageView[][] tab
    private GridView gridView;

    public ImageViewAdapter(Context c, ArrayList<ImageView> imageList, GridView gridView)
    {
        this.mContext = c;
        this.imageList = imageList;
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        return imageList != null ? imageList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return imageList != null ? imageList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return imageList != null ? position : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = null;

        if (convertView == null) { // if it's a new iv
            iv = this.imageList.get(position);
            iv.setLayoutParams(new GridView.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth()));
            /*ColorDrawable drawable = (ColorDrawable) this.imageList.get(position).getBackground(); // get color from imageList array
            if (drawable != null)
                iv.setBackgroundColor(drawable.getColor());
            else
                iv.setBackgroundColor(Color.BLACK);*/
        } else {
            iv = (ImageView) convertView;
        }

        return iv;
    }

}
