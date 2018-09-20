package cz.kalina.stampie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.adapters.ImageClassAdapter;
import cz.kalina.stampie.data.entities.Stamp;
import cz.kalina.stampie.data.entities.User;
import cz.kalina.stampie.pages.MapActivity;
import cz.kalina.stampie.pages.PhotosActivity;
import cz.kalina.stampie.pages.ReviewsActivity;
import cz.kalina.stampie.pages.StampsActivity;
import cz.kalina.stampie.pages.StatisticsActivity;
import cz.kalina.stampie.utils.AlertDialogThread;
import cz.kalina.stampie.utils.Config;
import cz.kalina.stampie.utils.ImageClassPair;
import cz.kalina.stampie.utils.NotificationReceiver;
import cz.kalina.stampie.utils.StampRecord;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {

    private static MainActivity instance = null;
    public static final Boolean isProVersion = true;

    private static final String stampsUrl = "https://www.turisticke-znamky.cz/export.php?item=1&type=csv";
    private static final int stampRecordColumnCount = 29;

    private final static String prefsFileName = "StampiePrefs";

    private static Database db = null;

    private static Activity activity = null;
    private static Context ctx = null;

    public static User activeUser = null;

    private static final List<ImageClassPair<Integer, Class>> pageIdentList = new ArrayList<ImageClassPair<Integer, Class>>(); {
        {
            new ImageClassPair(R.drawable.button_icon_map, MapActivity.class);
            new ImageClassPair(R.drawable.button_icon_stamps, StampsActivity.class);
            new ImageClassPair(R.drawable.button_icon_stats, StatisticsActivity.class);
            new ImageClassPair(R.drawable.button_icon_photos, PhotosActivity.class);
            new ImageClassPair(R.drawable.button_icon_reviews, ReviewsActivity.class);
        }
    };

    public static MainActivity getInstance() {

        if (instance == null) {
            instance = new MainActivity();
        }

        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        instance = this;

        new Config(this, this.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridview = (GridView)findViewById(R.id.gridview);
        gridview.setAdapter(new ImageClassAdapter(this, pageIdentList));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "Page selected: " + position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.getInstance(), (Class)pageIdentList.get(position).getPageClass()));
            }
        });

        //startActivity(new Intent(MainActivity.getInstance(), MapActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_action_settings) {
            return true;
        }

        if (id == R.id.menu_action_about) {

            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_about, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);
            builder.setMessage("(@) Copyright Kalina 2018").setTitle("About");
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Database getDb() {
        return MainActivity.db;
    }

    public void showMap(View view) {
        startActivity(new Intent(MainActivity.getInstance(), MapActivity.class));
    }

    public void showStamps(View view) {
        startActivity(new Intent(MainActivity.getInstance(), StampsActivity.class));
    }

    public void showPhotos(View view) {
        //startActivity(new Intent(MainActivity.getInstance(), PhotosActivity.class));
    }

    public void showGames(View view) {
        //startActivity(new Intent(MainActivity.getInstance(), GamesActivity.class));
    }

    public void showComments(View view) {
        //startActivity(new Intent(MainActivity.getInstance(), ReviewsActivity.class));
    }

    public void showStatistics(View view) {
        //startActivity(new Intent(MainActivity.getInstance(), StatisticsActivity.class));
    }

    public static void NotifyUpload() {
        notify("mVITAKARTA synchronizace dat", "Ukládání příchozích změn dat na server...");
    }

    public static void NotifyDownload() {
        notify("mVITAKARTA synchronizace dat", "Stahování dat ze serveru...");
    }

    public static void NotifySaving() {
        notify("mVITAKARTA synchronizace dat", "Ukládání dat do databáze...");
    }

    public static void NotifyFinish() {
        notify("mVITAKARTA synchronizace dat", "Hotovo");
    }

    public static void NotifyError(String msg) {
        notify("mVITAKARTA synchronizace dat", "Chyba synchronizace: " + msg);
    }

    public static void NotifyDelete(String rc) {
        notify("mVITAKARTA synchronizace dat", "Pojištěnec s RČ='" + rc + "' byl smazán");
    }

    public static void NotifyDownloadResult(Boolean success) {
        notify("mVITAKARTA synchronizace dat", "Stažení dat pojištěnce bylo " + (success ? "" : "ne") + "úspěšné", true);
    }

    private static void notify(String title, String message) {
        notify(title, message, false);
    }

    private static void notify(String title, String message, boolean sound) {

        try {

            Context ctx = MainActivity.getContext();
            Activity act = MainActivity.getActivity();

            Intent intent = new Intent(MainActivity.getContext(), NotificationReceiver.class);
            PendingIntent pIntent = PendingIntent.getActivity(MainActivity.getContext(), (int)System.currentTimeMillis(), intent, 0);

            Notification notification = new Notification.Builder(MainActivity.getContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.downnotify)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)act.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

        } catch (Exception ex) {
            Log.e("Vitakarta", "Doslo k chybe pri notifikaci zpravy: " + ex.getMessage());
        }
    }

    public static Activity getActivity() {
        return activity;
    }

    public static Context getContext() {
        return ctx;
    }

    public static void reportError(Context ctx, String message, String cause) {

        try {
            AlertDialogThread thread = new AlertDialogThread(ctx, "Error", message);
            thread.show();
        } catch (Throwable t) {}

        Log.e("Vitakarta", message + ": " + cause);
    }

    public static void reportError(Context ctx, String message) {

        try {
            AlertDialogThread thread = new AlertDialogThread(ctx, "Error", message);
            thread.show();
        } catch (Throwable t) {}

        Log.e("Vitakarta", message);
    }

    public static void reportInfo(Context ctx, String message) {

        try {
            AlertDialogThread thread = new AlertDialogThread(ctx, "Info", message);
            thread.show();
        } catch (Throwable t) {}

        Log.i("Vitakarta", message);
    }

    public static void reportUserMessage(Context ctx, String title, String message) {

        try {
            AlertDialogThread thread = new AlertDialogThread(ctx, title, message);
            thread.show();
        } catch (Throwable t) {}

        Log.i("Vitakarta", message);
    }

    public static void reportUserMessageSimple(Context ctx, String title, String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder = builder.setTitle(title).setMessage(message);
            builder = builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.show();
            if (dialog != null) {
                TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                if (messageText != null) messageText.setGravity(Gravity.CENTER);
            }
        } catch (Exception ex) {
            Log.e("Vitakarta","Chyba pri zobrazeni uzivatelskeho okna: " + ex.getMessage());
        }
    }

    public static void reportUserMessageSimple(Activity act, String title, String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder = builder.setTitle(title).setMessage(message);
            builder = builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.show();
            if (dialog != null) {
                TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
                if (messageText != null) messageText.setGravity(Gravity.CENTER);
            }
        } catch (Exception ex) {
            Log.e("Vitakarta","Chyba pri zobrazeni uzivatelskeho okna: " + ex.getMessage());
        } catch (Throwable th) {
            Log.e("Vitakarta","Chyba pri zobrazeni uzivatelskeho okna");
        }
    }

    public static void OpenDb() throws Exception {

        try {

            // inicializace databaze
            if (db == null) db = new Database(MainActivity.ctx);
            db.Open();

        } catch (Exception ex) {
            Log.e("Vitakarta", "Došlo k chybě při připojení k DB: " + ex.toString());
        }
    }

    public static void CloseDb() {

        try {

            // deinicializace databaze
            if (db != null) db.Close();

        } catch (Exception ex) {
            Log.e("Vitakarta", "Došlo k chybě při odpojení od DB: " + ex.toString());
        }
    }

    public static void ClearPrefs() throws Exception {

        // smaz vse krome hesla
/*
        String heslo = Config.getHeslo("");
        boolean poStartu = Config.getAutoSync(true);
        Config.removeAll();
        Config.putHeslo(heslo);
        Config.putAutoSync(poStartu);
*/
    }

    public static List<StampRecord> GetAllStamps() {

        List<StampRecord> stamps = new ArrayList<StampRecord>();

        try {

            HttpURLConnection conn = null;
            try {
                URL url = new URL(stampsUrl);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                // read first line with column titles
                String line = br.readLine();

                // read rest of document
                while ((line = br.readLine()) != null) {

                    if (line != null) {
                        stamps.add(parseStampRecord(line));
                    }
                }

            } catch (Exception ex) {
                throw ex;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            Log.i("Stampie", "There was downloaded " + stamps.size() + " stamps from server");

        } catch (Exception e) {
            Log.e("Stampie", "Error occured when downloading data into map: " + e.getMessage());
        }

        return stamps;
    }

    public static StampRecord parseStampRecord(String stampLine) {

        StampRecord record = new StampRecord();

        String[] stampLineParts = stampLine.split(";");
        if (stampLineParts.length < stampRecordColumnCount) {

        } else {

            record.stampId = Integer.parseInt(stampLineParts[0]);
            record.name = stampLineParts[1];
            record.category = stampLineParts[2];
            record.county = stampLineParts[3];

            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(stampLineParts[4]);
            } catch (ParseException ex) {}
            if (date != null) { record.published = date; }

            record.sellingPlace1 = stampLineParts[5];
            record.sellingPlace1Web = stampLineParts[6];
            record.sellingPlace2 = stampLineParts[7];
            record.sellingPlace2Web = stampLineParts[8];
            record.sellingPlace3 = stampLineParts[9];
            record.sellingPlace3Web = stampLineParts[10];
            record.sellingPlace4 = stampLineParts[11];
            record.sellingPlace4Web = stampLineParts[12];
            record.sellingPlace5 = stampLineParts[13];
            record.sellingPlace5Web = stampLineParts[14];
            record.sellingPlace6 = stampLineParts[15];
            record.sellingPlace6Web = stampLineParts[16];
            record.sellingPlace7 = stampLineParts[17];
            record.sellingPlace7Web = stampLineParts[18];
            record.sellingPlace8 = stampLineParts[19];
            record.sellingPlace8Web = stampLineParts[20];
            record.sellingPlace9 = stampLineParts[21];
            record.sellingPlace9Web = stampLineParts[22];
            record.sellingPlace10 = stampLineParts[23];
            record.sellingPlace10Web = stampLineParts[24];
            record.sellingPlace11 = stampLineParts[25];
            record.sellingPlace11Web = stampLineParts[26];
            record.gpsPositionLat = Double.parseDouble(stampLineParts[27]);
            record.gpsPositionLng = Double.parseDouble(stampLineParts[28]);
        }

        return record;
    }
}
