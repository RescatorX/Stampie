package cz.kalina.stampie.pages;

import java.util.ArrayList;
import java.util.List;

import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cz.kalina.stampie.data.adapters.StampListAdapter;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.dao.intf.IStampDAO;
import cz.kalina.stampie.data.entities.Stamp;
import cz.kalina.stampie.R;
import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.utils.StampRecord;

public class StampsActivity extends ListActivity implements OnChildClickListener {

    private static List<Stamp> stamps = null;
    private IStampDAO stampDao = null;
    private StampListAdapter adapter = null;
    private List<StampRecord> stampRecords = new ArrayList<StampRecord>();

    public static StampsActivity stampsActivity = null;

    private Button addBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_stamps);

            stampDao = DAOFactory.getInstance().getStampDAO();

            //addBtn = (Button)findViewById(R.id.OckovaniAddBtn);
            //addBtn.setOnClickListener(addButtonListener);

        } catch (Exception e){
            MainActivity.reportError(this, "Error occured when stamps list initialization", e.getMessage());
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        try {

            getStamps();

        } catch (Exception e) {
            //MainActivity.reportError(this, "Error occured when stamps list initilization", e.getMessage());
        }
    }

    private OnClickListener addButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                Intent addIntent = new Intent(StampsActivity.this, StampsEditActivity.class);
                addIntent.putExtra("ID", 0);
                startActivity(addIntent);

            } catch (Exception e) {
                MainActivity.reportError(StampsActivity.this, "Error occured when starting stamp edit", e.getMessage());
            }

        }
    };

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        try {

            Stamp stamp = (Stamp)v.getTag();
            if (stamp == null) throw new Exception("Cannot obtain stamp ID for editing");

            Intent editIntent = new Intent(this, StampsEditActivity.class);
            editIntent.putExtra("ID", stamp.getId());
            startActivityForResult(editIntent, R.id.PageStampsEdit);
            return false;

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when showing stamp detail", e.getMessage());
        }
        return false;
    }

    private void getStamps() {

        try {

            stamps = new ArrayList<Stamp>();
            if (stampDao != null) {
                //stamps = stampDao.findAll(MainActivity.getDb());

                stampsActivity = this;

                // startujeme working thread
                Thread download = new Thread(new Runnable() {

                    public void run() {

                        try {

                            Log.i("Stampie", "Start downloading stamps data 1");
                            stampRecords = MainActivity.GetAllStamps();

                            loadedHandler.sendMessage(new Message());

                        } catch (Throwable t) {
                            Log.e("Stampie", "Error occured in thred of download data for map: " + t.getMessage());
                        }
                    }
                });
                download.start();
            }

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when reading stamp list", e.getMessage());
        }

        runOnUiThread(returnRes);
    }

    public static Handler loadedHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            try {

                if (stampsActivity.stampRecords != null) {
                    for (StampRecord stampRecord : stampsActivity.stampRecords) {
                        stamps.add(new Stamp(stampRecord));
                    }

                    stampsActivity.adapter = new StampListAdapter(stampsActivity, stampsActivity.stampDao, MainActivity.activeUser, stamps);

                    stampsActivity.setListAdapter(stampsActivity.adapter);
                }

            } catch (Throwable th) {
                Log.e("Stampie", "Error occured when reading stamps: " + th.getMessage());
            }
        }
    };

    private Runnable returnRes = new Runnable() {

        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            switch (requestCode) {
                case R.id.PageStampsEdit :
                {
                    if (resultCode == RESULT_OK) {

                        onStart();
                    }
                }
                break;
            }

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when returning from another activity", e.toString());
        }
    }
}
