package cz.kalina.stampie.pages;

import java.io.File;
import java.text.NumberFormat;

import android.animation.TypeConverter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.R;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.dao.intf.IPhotoDAO;
import cz.kalina.stampie.data.entities.Photo;
import cz.kalina.stampie.utils.Config;

public class PhotosEditActivity extends FragmentActivity {

    private Button camBtn = null;
    private Button galBtn = null;
    private Button cncBtn = null;

    private final String imageFilePrefix = "StampieImg_";
    private Integer imageFileIndex = 0;

    private Uri imageUri = null;
    private File imageFile = null;
    private long photoId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_edit);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) { photoId = bundle.getInt("photoId");}

            camBtn = (Button)findViewById(R.id.PhotosEditCameraBtn);
            camBtn.setOnClickListener(cameraButtonListener);

            galBtn = (Button)findViewById(R.id.PhotosEditGalleryBtn);
            galBtn.setOnClickListener(galleryButtonListener);

            cncBtn = (Button)findViewById(R.id.PhotosEditCancelBtn);
            cncBtn.setOnClickListener(cancelButtonListener);

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when photo editing initialization", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            switch (requestCode) {
                case R.id.PhotosEditCameraBtn :
                {
                    if (resultCode == RESULT_OK) {

                        //use imageUri here to access the image
                        if (imageUri != null) imageFile = convertCameraImageUriToFile(imageUri);

                        Intent spIntent = new Intent(PhotosEditActivity.this, PhotosTitleActivity.class);
                        startActivityForResult(spIntent, R.id.PagePhotosTitle);

                    } else {

                        if (resultCode == RESULT_CANCELED) {

                            imageFile = null;
                        }

                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                break;
                case R.id.PhotosEditGalleryBtn :
                {
                    if (resultCode == RESULT_OK) {

                        imageFile = convertGaleryImageUriToFile(data.getData());

                        Intent spIntent = new Intent(PhotosEditActivity.this, PhotosTitleActivity.class);
                        startActivityForResult(spIntent, R.id.PagePhotosTitle);

                    } else {

                        if (resultCode == RESULT_CANCELED) {
                            imageFile = null;
                        }

                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                break;
                case R.id.PagePhotosTitle :
                {
                    if (resultCode == RESULT_OK) {

                        String title = "";

                        Bundle extras = data.getExtras();
                        if (extras != null) title = extras.getString("Title");

                        try {

                            IPhotoDAO photoDao = DAOFactory.getInstance().getPhotoDAO();

                            Photo photo = photoDao.findById(photoId, MainActivity.getDb());
                            photo.setName(title);

                            photoDao.update(photoId, photo, MainActivity.getDb());

                        } catch (Exception e) {

                            String msg = e.getMessage().toLowerCase();
                            if (msg.contains("outofmemoryexception") || msg.contains("outofmemoryerror"))
                                MainActivity.reportUserMessage(PhotosEditActivity.this, "Attention", "The photo is too big for saving in your device.");
                            else
                                MainActivity.reportUserMessage(PhotosEditActivity.this, "Attention", "The photo cannot be saved, error occured.");

                            setResult(RESULT_CANCELED);
                            finish();

                            return;
                        }

                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;
            }

        } catch (Exception e) {
            MainActivity.reportError(PhotosEditActivity.this, "Došlo k chybě při přidání nového souboru", e.toString());
        }
    }

    private File convertCameraImageUriToFile(Uri imageUri)  {

        Cursor cursor = null;

        try {

            String [] proj = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION };

            cursor = managedQuery( imageUri,
                    proj, // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null); // Order-by clause (ascending by name)

            if (cursor.moveToFirst()) return new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

            return null;

        } finally {
            //if (cursor != null) cursor.close(); !!! nesmi se zavirat, jinak to pada !!!
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    public File convertGaleryImageUriToFile(Uri imageUri) {

        Cursor cursor = null;

        try {
            // can post image
            String [] proj={MediaStore.Images.Media.DATA};
            cursor = managedQuery(imageUri,
                    proj, // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null); // Order-by clause (ascending by name)
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return new File(cursor.getString(column_index));

        } finally {
            //if (cursor != null) cursor.close();	!!! nesmi se zavirat, jinak to pada !!!
        }
    }

    private OnClickListener cameraButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                if (!Config.getUseCard(true)) {

                    MainActivity.reportUserMessage(PhotosEditActivity.this, "Upozornění", "Fotoaparát lze použít pouze spolu s paměťovou kartou");
                    return;
                }

                // je uz heslo ulozeno ?
                if (!Config.getExists(Config.MCAM_IMID)) {

                    Config.putCameraImageId(1);
                    imageFileIndex = 1;

                } else {

                    imageFileIndex = Config.getCameraImageId(1);
                    imageFileIndex++;
                    Config.putCameraImageId(imageFileIndex);
                }

                //define the file-name to save photo taken by Camera activity
                NumberFormat form = NumberFormat.getInstance();
                form.setMinimumIntegerDigits(4);
                String fileName = imageFilePrefix + form.format((long)imageFileIndex);

                //create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Vyfotit");

                //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                try {
                    imageUri = getContentResolver().insert(Config.getUseCard(true) ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
                } catch (Exception e) {
                    Log.e("Vitakarta", "Chyba SouboryEdit: " + e.getMessage(), e);
                    MainActivity.reportUserMessage(PhotosEditActivity.this, "Upozornění", "Zařízení nemá přístup k úložišti souborů, pořízený snímek by nebylo možné uložit" + (Config.getUseCard(true) ? ", zkontrolujte přítomnost paměťové karty." : "."));
                    return;
                }

                //create new Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, R.id.PhotosEditCameraBtn);

            } catch (Exception e) {
                MainActivity.reportError(PhotosEditActivity.this, "Error occured when saving new photo", e.toString());
            }

        }
    };

    private OnClickListener galleryButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Vybrat snímek"), R.id.SouboryEditGaleryBtn);

            } catch (Exception e) {
                MainActivity.reportError(PhotosEditActivity.this, "Došlo k chybě při uložení nového souboru", e.toString());
            }

        }
    };

    private OnClickListener cancelButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                setResult(RESULT_CANCELED);
                finish();

            } catch (Exception e) {
                MainActivity.reportError(PhotosEditActivity.this, "Došlo k chybě při zrušení editace souboru", e.getMessage());
            }
        }
    };
}
