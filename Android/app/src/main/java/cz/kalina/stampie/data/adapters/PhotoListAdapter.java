package cz.kalina.stampie.data.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cz.kalina.stampie.R;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.dao.intf.IPhotoDAO;
import cz.kalina.stampie.data.entities.Photo;
import cz.kalina.stampie.data.entities.User;

public class PhotoListAdapter extends ArrayAdapter<Photo> {

    private List<Photo> items;
    private IPhotoDAO photoDao = null;
    private User currentUser = null;

    public PhotoListAdapter(Context context, User currentUser, List<Photo> items) {
        super(context, 0, items);

        this.currentUser = currentUser;
        this.items = items;

        photoDao = DAOFactory.getInstance().getPhotoDAO();
    }

    public int getCount() {
        return items.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    public void SetItems(List<Photo> items) {
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_photos_item, null);
        }
        Photo photo = items.get(position);
        if (photo != null) {

            try {

                v.setTag(photo);

                ImageView imageView = (ImageView)v.findViewById(R.id.PhotosItemImage);
                TextView  descView = (TextView)v.findViewById(R.id.PhotosItemDescription);
                TextView  titleView = (TextView)v.findViewById(R.id.PhotosItemTitle);
                TextView  lengthView = (TextView)v.findViewById(R.id.PhotosItemLength);

                if (imageView != null)	{
                    imageView.setImageResource((photo.getCreator().getId() == currentUser.getId()) ? R.drawable.photo_show : R.drawable.photo);
                }

                if (descView != null) descView.setText((photo.getDescription() != null) ? photo.getDescription() : "{no description}");
                if (titleView != null) titleView.setText("Title: " + ((photo.getName() != null) ? photo.getName() : "{no title}"));
                if (lengthView != null)	{

                    Integer idel = (int)(photo.getDataLength() / 1024);

                    lengthView.setText("Size: " + idel.toString() + " kB");
                }

            } catch (Exception e) {
                Log.e(PhotoListAdapter.class.getName(), "Error occured when displaying photo item", e);
            }
        }
        return v;
    }
}
