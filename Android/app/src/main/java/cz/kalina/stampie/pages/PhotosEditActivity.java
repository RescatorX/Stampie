package cz.kalina.stampie.pages;

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

import java.io.File;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.R;
import cz.kalina.stampie.utils.Config;

public class PhotosEditActivity extends FragmentActivity {

    private Button camBtn = null;
    private Button galBtn = null;
    private Button cncBtn = null;

    private final String imageFilePrefix = "StampieImg_";
    private Integer imageFileIndex = 0;

    private Uri imageUri = null;
    private File imageFile = null;
    private int cislo1 = 0;
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_soubory_edit);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {cislo1 = bundle.getInt("cislo1");}

            camBtn = (Button)findViewById(R.id.SouboryEditCameraBtn);
            camBtn.setOnClickListener(cameraButtonListener);

            galBtn = (Button)findViewById(R.id.SouboryEditGaleryBtn);
            galBtn.setOnClickListener(galleryButtonListener);

            cncBtn = (Button)findViewById(R.id.SouboryEditCnclBtn);
            cncBtn.setOnClickListener(cancelButtonListener);

        } catch (Throwable th) {
            MainActivity.reportError(this, "Došlo k chybě při inicializaci stránky editace očkování uživatele", th.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            switch (requestCode) {
                case R.id.SouboryEditCameraBtn :
                {
                    if (resultCode == RESULT_OK) {

                        //use imageUri here to access the image
                        if (imageUri != null) imageFile = convertCameraImageUriToFile(imageUri);

                        Intent spIntent = new Intent(SouboryEdit.this, SouboryPopis.class);
                        startActivityForResult(spIntent, R.id.PageSouboryPopis);

                    } else {

                        if (resultCode == RESULT_CANCELED) {

                            imageFile = null;
                        }

                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                break;
                case R.id.SouboryEditGaleryBtn :
                {
                    if (resultCode == RESULT_OK) {

                        imageFile = convertGaleryImageUriToFile(data.getData());

                        Intent spIntent = new Intent(SouboryEdit.this, SouboryPopis.class);
                        startActivityForResult(spIntent, R.id.PageSouboryPopis);

                    } else {

                        if (resultCode == RESULT_CANCELED) {

                            imageFile = null;
                        }

                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                break;
                case R.id.PageSouboryPopis :
                {
                    if (resultCode == RESULT_OK) {

                        String popis = "";

                        Bundle extras = data.getExtras();
                        if (extras != null) popis = extras.getString("Popis");

                        if (pm == null) {

                            if (VitakartaActivity.activeUser == null) throw new Exception("Neni k dispozici aktualni uzivatel");
                            if ((VitakartaActivity.activeUser.getId_poj() == null) ||
                                    (VitakartaActivity.activeUser.getId_poj().equals(0))) throw new Exception("Neni k dispozici ID aktualniho uzivatele");

                            if (pm == null) pm = PojMemoManager.getInstance(VitakartaActivity.activeUser.getId_poj());
                        }

                        if (pm != null) {

                            String key = "VK_" + TypeConverter.dateTime2StrDensed(Calendar.getInstance());

                            try {

                                pm.addSoubory(popis, VitakartaActivity.activeUser.getRodcis(), key, imageFile, cislo1);

                            } catch (Exception e) {

                                // ulozeni se nepovedlo
                                String msg = e.getMessage().toLowerCase();
                                if (msg.contains("outofmemoryexception") || msg.contains("outofmemoryerror"))
                                    VitakartaActivity.reportUserMessage(SouboryEdit.this, "Upozornění", "Soubor je pro zpracovaní ve Vašem zařízení příliš velký. Bližší informace naleznete v nápovědě.");
                                else
                                    VitakartaActivity.reportUserMessage(SouboryEdit.this, "Upozornění", "Nelze korektně uložit obsah stahovaného souboru, došlo k chybě komunikace se serverem.");

                                setResult(RESULT_CANCELED);
                                finish();

                                return;
                            }
                        }

                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;
            }

        } catch (Throwable th) {
            VitakartaActivity.reportError(SouboryEdit.this, "Došlo k chybě při přidání nového souboru", th.toString());
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

                    VitakartaActivity.reportUserMessage(SouboryEdit.this, "Upozornění", "Fotoaparát lze použít pouze spolu s paměťovou kartou");
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
                    VitakartaActivity.reportUserMessage(SouboryEdit.this, "Upozornění", "Zařízení nemá přístup k úložišti souborů, pořízený snímek by nebylo možné uložit" + (Config.getUseCard(true) ? ", zkontrolujte přítomnost paměťové karty." : "."));
                    return;
                }

                //create new Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, R.id.SouboryEditCameraBtn);

            } catch (Exception ex) {
                VitakartaActivity.reportError(SouboryEdit.this, "Došlo k chybě při uložení nového souboru", ex.toString());
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

            } catch (Exception ex) {
                VitakartaActivity.reportError(SouboryEdit.this, "Došlo k chybě při uložení nového souboru", ex.toString());
            }

        }
    };

    private OnClickListener cancelButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                setResult(RESULT_CANCELED);
                finish();

            } catch (Exception ex) {
                VitakartaActivity.reportError(SouboryEdit.this, "Došlo k chybě při zrušení editace souboru", ex.getMessage());
            }

        }
    };
*/
}
