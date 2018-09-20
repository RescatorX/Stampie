package cz.kalina.stampie.data.adapters;

import java.util.List;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cz.kalina.stampie.R;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.dao.intf.IStampDAO;
import cz.kalina.stampie.data.entities.Stamp;
import cz.kalina.stampie.data.entities.User;

public class StampListAdapter extends ArrayAdapter<Stamp> {

    private List<Stamp> items;
    private IStampDAO stampDao = null;
    private User currentUser = null;

    public StampListAdapter(Context context, IStampDAO stampDao, User currentUser, List<Stamp> items) {
        super(context, 0, items);

        this.stampDao = stampDao;
        this.currentUser = currentUser;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    public void SetItems(List<Stamp> items) {
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_stamps_item, null);
        }
        Stamp stamp = items.get(position);
        if (stamp != null) {

            try {

                v.setTag(stamp);

                ImageView imageView = (ImageView)v.findViewById(R.id.StampsItemImage);
                TextView  nameView = (TextView)v.findViewById(R.id.StampsItemName);
                TextView  categoryView = (TextView)v.findViewById(R.id.StampsItemCategory);
/*
                if (imageView != null)	{
                    imageView.setImageResource((stamp.getCreator().getId() == currentUser.getId()) ? R.drawable.photo_show : R.drawable.photo);
                }
*/
                if (nameView != null) nameView.setText((stamp.getName() != null) ? stamp.getName().replace("\"", "") : "{no name}");
                if (categoryView != null) categoryView.setText(((stamp.getCategory() != null) ? stamp.getCategory().replace("\"", "") : "{no category}"));
/*
                ((SwipeRefreshLayout)v).setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                String msg = "Refreshed";
                            }
                        }
                );
*/
            } catch (Exception e) {
                Log.e(StampListAdapter.class.getName(), "Error occured when displaying stamp item", e);
            }
        }
        return v;
    }
}
