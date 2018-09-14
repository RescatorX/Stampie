package cz.kalina.stampie.pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cz.kalina.stampie.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.kalina.stampie.R;
import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.utils.Config;
import cz.kalina.stampie.data.entities.Photo;
import cz.kalina.stampie.utils.STPException;
import cz.kalina.stampie.data.dao.intf.*;
import cz.kalina.stampie.data.dao.impl.*;

public class PhotosDetailActivity extends FragmentActivity {

    public static String fileName = null;
    private long photoId = 0;
    private Photo photo = null;

    private Button updBtn = null;
    private Button delBtn = null;
    private Button cncBtn = null;
    public static Button shwBtn = null;

    public static ImageView imgView = null;
    public static TextView sizeView = null;
    public static Bitmap bm = null;
    private static File file = null;

    private IPhotoDAO photoDao = null;

    private static Boolean downloaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_detail);

        try {
            updBtn = (Button)findViewById(R.id.PhotosDetailUpdateBtn);
            updBtn.setOnClickListener(updButtonListener);

            delBtn = (Button)findViewById(R.id.PhotosDetailDeleteBtn);
            delBtn.setOnClickListener(delButtonListener);

            cncBtn = (Button)findViewById(R.id.PhotosDetailCancelBtn);
            cncBtn.setOnClickListener(cncButtonListener);

            shwBtn = (Button)findViewById(R.id.PhotosDetailShowBtn);
            shwBtn.setOnClickListener(shwButtonListener);

            imgView = (ImageView)findViewById(R.id.PhotosDetailImage);
            sizeView = (TextView)findViewById(R.id.PhotosDetailSize);

            photoDao = DAOFactory.getInstance().getPhotoDAO();

            Bundle bundle = getIntent().getExtras();

            if (photoId == 0) photoId = bundle.getLong("photoId");

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when photo detail initialization", e.toString());
        }

    }

    @Override
    public void onStart() {

        super.onStart();

        try {

            if (imgView != null) {
                imgView.setImageBitmap(null);
            }

            if (photo == null) {
                photo = photoDao.findById(photoId, MainActivity.getDb());
            }

            if (photo == null) throw new Exception("Cannot obtain photo object by Id from the database");

            fileName = photo.getName().toLowerCase();

            // na velkem tlacitku se zobrazi budto vyzva ke stazeni nebo k zobrazeni v ext.prohlizeci
            if (sizeView != null) {

                int idel = (int)(photo.getDataLength() / 1024);

                sizeView.setText("Velikost: " + Integer.toString(idel) + " kB");
            }

            // zjistime stav stazeni souboru
            if (MainActivity.activeUser == null) throw new Exception("Cannot obtain current user");
/*
            String key = null;
            soubor = sm.loadSouborByRodcisAndMemIdJOrParKey(poznamka.getId_pojmemo_j(), VitakartaActivity.activeUser.getRodcis(), poznamka.getText3().replace("VK_", "EN_"));
            if (soubor != null) key = soubor.getPar_key();
            else key = poznamka.getText3();

            downloaded = false;
            if (key.startsWith("EN_")) downloaded = SouborManager.isStored(key.replace("VK_", "EN_"));

            if (downloaded) {

                // rozsifrujeme ulozeny zasifrovany soubor
                String encPath = SouborManager.completePathName(key.replace("VK_", "EN_"));
                if (encPath == null) {

                    downloaded = false;
                    shwBtn.setText("Stáhnout soubor ze serveru");
                    return;
                }

                fEnc = new File(encPath);
                if (fEnc.exists()) {

                    // startujeme working thread
                    Thread download = new Thread(new Runnable() {

                        @SuppressLint("WorldReadableFiles")
                        public void run() {

                            try {

                                // Create encrypter/decrypter class
                                DesEncrypter encrypter = new DesEncrypter();

                                // Decrypt
                                if (Config.getUseCard(false)) {

                                    fDec = new File(SouborManager.completeDecPathName(fileName));
                                    if (fDec.exists()) fDec.delete();
                                    fDec.createNewFile();
                                    encrypter.decrypt(new FileInputStream(fEnc), new FileOutputStream(fDec));

                                } else {

                                    encrypter.decrypt(new FileInputStream(fEnc), openFileOutput(fileName, Context.MODE_WORLD_READABLE));

                                    fDec = getFileStreamPath(fileName);
                                }

                                fileDecHandler.sendMessage(new Message());

                            } catch (Throwable t) {

                                fDec = null;
                                fileDecHandler.sendMessage(new Message());
                            }
                        }
                    });

                    decPending = true;
                    download.start();
                }

                shwBtn.setText("Zobrazit v externím prohlížeči");
                shwBtn.setEnabled(false);

            } else {

                shwBtn.setText("Stáhnout soubor ze serveru");
            }
*/
        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when photo detail initialization", e.getMessage());
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        try {
            if (imgView != null) imgView.setImageBitmap(null);
        } catch (Throwable th) {}

        try {
            if (bm != null) {

                bm.recycle();
                bm = null;
            }
        } catch (Throwable th) {}

        System.gc();
    }

    private void ShowImg(String pathName) {

        try {

            if ((photoId != 0) && (imgView != null) && (pathName != null)) {

                BitmapFactory.Options o = new BitmapFactory.Options();
                //o.inJustDecodeBounds = true;
                o.inSampleSize = 4;

                if (!(new File(pathName)).exists()) return;

                bm = BitmapFactory.decodeFile(pathName, o);
                if (bm == null) return;

                imgView.setImageBitmap(bm);
            }

        } catch (Exception e) {
            MainActivity.reportError(this, "Error occured when photo showing", e.getMessage());
        }
    }

    private static void ShowImg(File fImg) {

        try {

            if ((imgView != null) && (fImg != null)) {

                BitmapFactory.Options o = new BitmapFactory.Options();
                //o.inJustDecodeBounds = true;
                o.inSampleSize = 2;

                if (!fImg.exists()) return;

                bm = BitmapFactory.decodeFile(fImg.getAbsolutePath(), o);
                if (bm == null) return;

                imgView.setImageBitmap(bm);
            }

        } catch (Exception e) {
            MainActivity.reportError(MainActivity.getContext(), "Error occured when photo showing", e.getMessage());
        }
    }

    private OnClickListener updButtonListener = new OnClickListener() {
        public void onClick(View v) {
/*
            try {

                if (Config.getSyncPending(false)) {

                    VitakartaActivity.reportUserMessage(SouboryDetail.this, "Upozornění", "Právě se stahují data pojištěnce, nelze ukládat nové změny");
                    return;
                }

                Intent presunIntent = new Intent(SouboryDetail.this, SouboryPresun.class);
                presunIntent.putExtra("pojmemoID", pojmemoID);
                startActivityForResult(presunIntent, R.id.PageSouboryPresun);

            } catch (Throwable ex) {
                VitakartaActivity.reportError(SouboryDetail.this, "Došlo k chybě při přesunu souboru", ex.toString());
            }
*/
        }
    };

    private OnClickListener delButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {
/*
                if (Config.getSyncPending(false)) {

                    VitakartaActivity.reportUserMessage(SouboryDetail.this, "Upozornění", "Právě se stahují data pojištěnce, nelze ukládat nové změny");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SouboryDetail.this);
                builder = builder.setMessage("Opravdu chcete soubor smazat ?").setPositiveButton("Ano", dialogClickListener);

                if (downloaded && poznamka.getId_pojmemo_j() != null && poznamka.getId_pojmemo_j() != 0) builder = builder.setNeutralButton("Jen obsah", dialogClickListener);

                builder.setNegativeButton("Ne" , dialogClickListener).show();
*/
            } catch (Throwable e) {
                MainActivity.reportError(PhotosDetailActivity.this, "Error occured when photo deleting", e.toString());
            }
        }
    };

    private OnClickListener cncButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                setResult(RESULT_CANCELED);
                finish();

            } catch (Exception e) {
                MainActivity.reportError(PhotosDetailActivity.this, "Error occured when photo detail closing", e.getMessage());
            }

        }
    };

    private OnClickListener shwButtonListener = new OnClickListener() {
        public void onClick(View v) {

            try {

                Log.i("Stampie", "shwButtonListener - start");

                if (photoId == 0) throw new Exception("Photo identifier not defined");

                if (MainActivity.activeUser == null) throw new Exception("Cannot obtain current user");

                Log.i("Stampie", "Photos IDs check");

                photo = photoDao.findById(photoId, MainActivity.getDb());
                if (photo == null) throw new Exception("Photo entity not found by Id");

                if (!downloaded) {
/*
                    Log.i("shwButtonListener", "Soubor neni stazen, kontaktujeme server");
                    if (!MainActivity.networkAvailable()) throw new Exception("Datové připojení není k dispozici");
                    if (Config.getSyncPending(false)) throw new Exception("Právě probíhá synchronizace dat");

                    if ((MainActivity.activeUser.getSign() == null) || "nosign".equals(MainActivity.activeUser.getSign())) {
                        Config.putSyncPending(false);
                        throw new Exception("Dosud nebyl dokončen proces autorizace");
                    }

                    // zkontrolujeme nutnost opetne autorizace
                    Log.i("shwButtonListener", "Pred testem autorizace");
                    if (MainActivity.activeUser.getAuth_sts() != 12) {

                        MainActivity.Authorize(SouboryDetail.this, MainActivity.activeUser, 10, true, true);

                        if (WSAuthorize.lastResultCode >= 99) {
                            Config.putSyncPending(false);
                            throw new Exception("Dosud nebyl dokončen proces autorizace");
                        }
                    }

                    try {

                        Log.i("Vitakarta", "Pred alokaci souboru");
                        fDec = new File(SouborManager.completeDecPathName(fileName));

                        // otestujeme si, zda je soubor zapisovatelny
                        if (!VitakartaActivity.CheckWriteBoolean(fDec.getParentFile())) {
                            shwBtn.setEnabled(false);
                            throw new Exception("Nelze uložit soubor do úložiště, zařízení nemá dostatek volné interní paměti nebo není vložena karta");
                        }

                        if (!fDec.exists()) fDec.createNewFile();

                        //thread.start();

                        // zjistime stav stazeni souboru
                        Log.i("Vitakarta", "Pred pouzitim SouborManageru");
                        if (sm == null) sm = SouborManager.getInstance(VitakartaActivity.activeUser.getId_poj());
                        if (sm == null) throw new Exception("Nelze inicializovat souborový manager");

                        final String key = "VK_" + TypeConverter.dateTime2StrDensed(Calendar.getInstance());

                        // ulozime stahovany soubor
                        try {

                            System.gc();

                            WSResult res = null;
                            WSFileDownload wsDown = null;
                            int poc = 3;
                            while (true) {

                                try {

                                    System.gc();

                                    Log.i("Vitakarta", "Pokus o stazeni souboru c." + (3-poc+1));
                                    wsDown = new WSFileDownload();
                                    wsDown.init(VitakartaActivity.getContext());
                                    wsDown.setActivity(SouboryDetail.this);
                                    wsDown.setSoubor(fDec);
                                    wsDown.setIdPojMemoJ(poznamka.getId_pojmemo_j());
                                    wsDown.execute(new String[] { "dejSoubor", VitakartaActivity.activeUser.getIdent(), VitakartaActivity.activeUser.getSign() });
                                    res = wsDown.get();
                                    Log.i("Vitakarta", "Stahovani souboru dokonceno");

                                    if (res.getCode() != null) break;
                                    else throw new WSException(res.getMessage());

                                } catch (Exception de) {

                                    Thread.sleep(1000);
                                    if (--poc <= 0) {
                                        Log.i("Vitakarta", "Zadny z pokusu o stazeni souboru nebyl uspesny");
                                        throw new WSException(de.getMessage());
                                    }
                                }
                            }
                            Log.i("Vitakarta", "Stahovani bylo uspesne");

                            if (wsDown.getStatus((SoapObject)res.getCode().getResponse())) {

                                //if ((new PojistenecData(SouboryDetail.this)).ulozSoubor(poznamka.getId_pojmemo_j(), fDec, SouboryDetail.this)) {

                                // zasifrujeme ukladany soubor, ale zatim nemazeme
                                if (fDec.exists()) {

                                    Log.i("Vitakarta", "Zasifrujeme stazeny soubor");
                                    // startujeme working thread
                                    Thread encrypt = new Thread(new Runnable() {

                                        public void run() {

                                            try {

                                                // Encrypt
                                                fEnc = new File(SouborManager.completePathName(key.replace("VK_", "EN_")));
                                                (new DesEncrypter()).encrypt(new FileInputStream(fDec), new FileOutputStream(fEnc));

                                                System.gc();

                                            } catch (Throwable t) {
                                            } finally {
                                                encPending = false;
                                                Log.i("Vitakarta", "Sifrovani souboru bylo dokonceno");
                                            }
                                        }
                                    });

                                    try {

                                        Log.i("Vitakarta", "Ulozime zaznam souboru do DB");

                                        decSoubor = new Soubor(VitakartaActivity.mdb.getDb());
                                        decSoubor.setId_pojmemo(poznamka.getId_pojmemo());
                                        decSoubor.setId_pojmemo_j(poznamka.getId_pojmemo_j());
                                        decSoubor.setRodcis(VitakartaActivity.activeUser.getRodcis());
                                        decSoubor.setPar_key(key.replace("VK_", "EN_"));
                                        //s.setEnc_key(strEnc);
                                        decSoubor.save();

                                        Log.i("Vitakarta", "Zaznam souboru do DB byl uspesne ulozen");

                                    } catch (Exception se) {
                                        Log.e("Vitakarta", "Zaznam souboru do DB se nepodarilo ulozit: " + se.getMessage());
                                        throw new DBException(se.getMessage());
                                    }

                                    Log.i("Vitakarta", "Start sifrovani stazeneho souboru");
                                    encPending = true;
                                    encrypt.start();

                                } else Log.e("Vitakarta", "Desifrovany soubor neexistuje");
                            } else Log.e("Vitakarta", "Result stahovani nelze parserovat");

                        } catch (WSException ex1) {

                            Thread.sleep(100);
                            shwBtn.setEnabled(false);
                            String msg = ex1.getMessage().toLowerCase();
                            if (msg.contains("outofmemoryexception") || msg.contains("outofmemoryerror"))
                                throw new Exception("Soubor je pro zpracovaní ve Vašem zařízení příliš velký. Bližší informace naleznete v nápovědě.");
                            else
                                throw new Exception("Nelze korektně stáhnout obsah souboru, došlo k chybě komunikace se serverem: " + ex1.getMessage());

                        } catch (DBException ex1) {

                            Thread.sleep(100);
                            shwBtn.setEnabled(false);
                            throw new Exception("Nepodařilo se uložit stahovaný soubor: " + ex1.getMessage());

                        } catch (Exception ex1) {

                            Thread.sleep(100);
                            shwBtn.setEnabled(false);
                            throw new Exception("Došlo k neznámé chybě při stahování nebo ukládání souboru: " + ex1.getMessage());
                        }

                        Log.i("Vitakarta", "Start zobrazeni souboru");
                        Thread.sleep(100);
                        downloaded = true;
                        shwBtn.setText("Zobrazit v externím prohlížeči");

                        fileName = poznamka.getText1().toLowerCase();
                        if ((fileName != null)) {

                            Boolean isBitmap = (fileName.endsWith("jpg")  ||
                                    fileName.endsWith("jpeg") ||
                                    fileName.endsWith("tiff") ||
                                    fileName.endsWith("tif")  ||
                                    fileName.endsWith("bmp")  ||
                                    fileName.endsWith("png")  ||
                                    fileName.endsWith("gif"));

                            if (isBitmap && (fDec != null)) {

                                try {
                                    ShowImg(fDec.getAbsolutePath());
                                } catch (Throwable t) {
                                    shwBtn.setEnabled(false);
                                    throw new Exception("Nelze korektně načíst obsah stahovaného souboru, používané zařízení zřejmě nemá dostatek volné paměti. Přehodnoťte nutnost stahování velkých souborů.");
                                } finally {
                                    System.gc();
                                }
                            }
                        }

                    } finally {
                        //thread.Dismiss();
                        //thread.Continue();
                        Log.i("Vitakarta", "Po (ne)uspesnem stazeni, ulozeni a sifrovani");
                    }
*/
                } else {

                    String appType = "application/*";

                    if (fileName.endsWith("jpg")  ||
                            fileName.endsWith("jpeg") ||
                            fileName.endsWith("tiff") ||
                            fileName.endsWith("tif")  ||
                            fileName.endsWith("bmp")  ||
                            fileName.endsWith("png")  ||
                            fileName.endsWith("gif")) appType = "image/*";

                    if (fileName.endsWith("mp3") ||
                            fileName.endsWith("wav")) appType = "audio/*";

                    if (fileName.endsWith("avi") ||
                            fileName.endsWith("mpg") ||
                            fileName.endsWith("mpeg") ||
                            fileName.endsWith("mp4")) appType = "video/*";

                    if (fileName.endsWith("pdf")) appType = "application/pdf";

                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, appType);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                }

            } catch (Exception em) {
                try {
                    MainActivity.reportUserMessageSimple(PhotosDetailActivity.this, "Upozornění", "Chyba při stahování nebo ukládání souboru: " + em.getMessage());
                } catch (Exception e1) {}
            } catch (Throwable th) {
                MainActivity.reportError(MainActivity.getContext(), "Chyba při stahování nebo ukládání souboru", th.getMessage());
            }
        }
    };

    public Handler fileDecHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            try {

                if ((fileName != null)) {

                    Boolean isBitmap = (fileName.endsWith("jpg")  ||
                            fileName.endsWith("jpeg") ||
                            fileName.endsWith("tiff") ||
                            fileName.endsWith("tif")  ||
                            fileName.endsWith("bmp")  ||
                            fileName.endsWith("png")  ||
                            fileName.endsWith("gif"));

                    if (isBitmap) {

                        try {

                            ShowImg(file);

                        } catch (Exception ex) {

                            MainActivity.reportUserMessage(MainActivity.getContext(), "Upozornění", "Nelze korektně načíst obsah stahovaného souboru: " + ex.getMessage());
                            shwBtn.setEnabled(false);

                        } finally {
                            System.gc();
                        }
                    }
                }

            } catch (Exception ex) {
                MainActivity.reportError(MainActivity.getContext(), "Došlo k chybě při dokončení šifrování staženého souboru", ex.getMessage());
            } finally {
                shwBtn.setEnabled(true);
            }
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    try {
                        if (photo != null) {

                            //photo.setDeleted(Calendar.getInstance());
                            //photo.save();

                            if (file != null) {

                                if (file.exists()) file.delete();
                            }
                        }
                    } catch (Exception e) {
                        MainActivity.reportError(PhotosDetailActivity.this, "Error occured when deleting photo", e.getMessage());
                    }

                    setResult(RESULT_OK);
                    finish();

                    break;

                case DialogInterface.BUTTON_NEUTRAL:

                    try {

                        if (file != null) {

                            if (file.exists()) file.delete();
                        }

                    } catch (Exception e) {
                        MainActivity.reportError(PhotosDetailActivity.this, "Error occured when deleting photo", e.getMessage());
                    }

                    setResult(RESULT_OK);
                    finish();

                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };
}
