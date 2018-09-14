package cz.kalina.stampie.pages;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import cz.kalina.stampie.R;
import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.dao.intf.*;
import cz.kalina.stampie.data.adapters.PhotosListAdapter;
import cz.kalina.stampie.data.entities.Photo;
import cz.kalina.stampie.utils.STPException;

public class PhotosActivity extends ListActivity {

    private List<Photo> photos = null;
    private PhotosListAdapter adapter = null;
    private long photoId = 0;
    private Photo currentPhoto = null;
    private IPhotoDAO photoDao = null;

    private Button addBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_photos);

            Bundle bundle = getIntent().getExtras();
            if (photoId == 0) photoId = bundle.getLong("photoId");

            if (photoId != 0 && currentPhoto == null) {

                photoDao = DAOFactory.getInstance().getPhotoDAO();
                currentPhoto = photoDao.findById(photoId, MainActivity.getDb());

                this.setTitle(Long.toString(currentPhoto.getId()));

                if (currentPhoto == null) throw new STPException("Cannot get Photo pbjekt from database");
            }

            addBtn = (Button)findViewById(R.id.PhotosAddBtn);
            addBtn.setOnClickListener(addButtonListener);

        } catch (Exception e){
            MainActivity.reportError(this, "Error occured when photos list loading initialization", e.getMessage());
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        try {

            photos = new ArrayList<Photo>();

            if (MainActivity.activeUser == null) throw new Exception("Cannot obtain current user object");

            adapter = new PhotosListAdapter(MainActivity.getContext(), MainActivity.activeUser, photos);
            setListAdapter(adapter);

            getPhotos();

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when photos list initialization", e.getMessage());
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

    private OnClickListener addButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                Intent editIntent = new Intent(PhotosActivity.this, PhotosEditActivity.class);
                startActivityForResult(editIntent, R.id.PagePhotosEdit);

            } catch (Exception e) {
                MainActivity.reportError(PhotosActivity.this, "Error occured when clicking photo add button", e.getMessage());
            }

        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        try {

            Photo photo = (Photo)v.getTag();
            if (photo == null) throw new Exception("Cannot obtain photo ID");

            Intent detailIntent = new Intent(this, PhotosDetailActivity.class);
            detailIntent.putExtra("photoId", photo.getId());
            startActivityForResult(detailIntent, R.id.PagePhotosDetail);

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when starting photo detail activity", e.getMessage());
        }

        super.onListItemClick(l, v, position, id);
    }

    private void getPhotos() {

        try {

            if (MainActivity.activeUser == null) throw new Exception("Cannot obtain current user object");

            photos = photoDao.findAll(MainActivity.getDb());

            adapter.SetItems(photos);

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when loading user photos list", e.getMessage());
        }

        runOnUiThread(returnRes);
    }

    private Runnable returnRes = new Runnable() {

        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            switch (requestCode) {
                case R.id.PagePhotosEdit :
                {
                    if (resultCode == RESULT_OK) {
                        onStart();
                    }
                }
                break;
            }

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when showing photos list", e.toString());
        }
    }
}