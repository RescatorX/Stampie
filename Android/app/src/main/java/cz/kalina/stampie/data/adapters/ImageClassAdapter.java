package cz.kalina.stampie.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import cz.kalina.stampie.R;
import cz.kalina.stampie.utils.ImageClassPair;

public class ImageClassAdapter extends BaseAdapter {

    private Context context;
    private List<ImageClassPair<Integer, Class>> pageIdentList;

    public ImageClassAdapter(Context context, List<ImageClassPair<Integer, Class>> pageIdentList) {
        this.context = context;
        this.pageIdentList = pageIdentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.buttons_main, null);

            // set image based on selected text
            ImageView imageView = (ImageView)gridView.findViewById(R.id.grid_item_image);

            imageView.setImageResource((Integer)pageIdentList.get(position).getPageId());

        } else {
            gridView = (View)convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return pageIdentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
