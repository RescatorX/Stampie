package cz.kalina.stampie.data;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.data.dao.DAOFactory;
import cz.kalina.stampie.data.entities.ExtData;
import cz.kalina.stampie.utils.STPException;

public class Database {

    public static Map<String, String> CisOdbornosti = new HashMap<String, String>();
    public static Map<String, String> CisUrSouhlasu = new HashMap<String, String>();
    public static Map<String, String> CisTypyZapisu = new HashMap<String, String>();
    public static Map<String, String> CisTypyKonfig = new HashMap<String, String>();

    private static final String dBName = "/data/data/cz.kalina.stampie/databases/StampieDB";
    private static final String dbVersion = "2018-09-01_12:00:00";

    protected SQLiteDatabase db = null;
    protected Context currentContext = null;
    protected Activity currentActivity = null;

    public Database() {

        currentContext = null;
    }

    public Database(Context ctx) throws STPException {

        if (ctx == null) throw new STPException("Cannot use without valid context");

        currentContext = ctx;
    }

    public Database(Context ctx, Activity act) throws STPException {

        if (ctx == null) throw new STPException("Cannot use without valid context");

        currentContext = ctx;
        currentActivity = act;
    }

    public Context getContext() {

        return currentContext;
    }

    public Activity getActivity() {

        return currentActivity;
    }

    public void Open() throws STPException {

        if (currentContext == null) throw new STPException("No valid context assigned");

        DatabaseHelper helper = null;
        try {

            helper = new DatabaseHelper(currentContext, dBName);
            db = helper.getWritableDatabase();
            // tohle je kvuli testu instalace DB, pri prvnim selectu to padne nachybu v catch bloku se nainstaluji tabulky a pokracuje se dal
            Boolean dbCheck = false;

            ExtData version = getVersion();
            if (version != null) {
                Log.i(Database.class.getName(), "OpenDB - verze DB je " + version.getText1());
                if (version.getText1().equals(dbVersion)) dbCheck = true;
            }

            if (!dbCheck) {
                helper.DropAllTables(db);
                helper.CreateAllTables(db);
                UpdateVersion();
            }

        } catch (Exception ex) {

            helper.DropAllTables(db);
            helper.CreateAllTables(db);
            UpdateVersion();
        }
    }

    private ExtData getVersion() {

        ExtData version = null;

        try {
            version = DAOFactory.getInstance().getExtDataDAO().getItemByType("0", this);
        } catch (Exception e) {
            version = null;
        }

        return version;
    }

    private void UpdateVersion() throws STPException {

        try {

            ExtData version = getVersion();
            if (version == null) {
                version = new ExtData();
                version.setType("0");
            }

            version.setText1(dbVersion);
            version.setCreated(Calendar.getInstance());
            DAOFactory.getInstance().getExtDataDAO().create(version, this);

        } catch (Exception ex) {
            MainActivity.reportError(currentContext, "Došlo k chybě při updatu verze databáze", ex.toString());
            throw new STPException("Version value save error: " + ex.getMessage(), ex);
        }
    }

    public void DeleteAllUsersData() {

        if (db != null) {
/*
            db.execSQL("DELETE FROM extdata");

            db.execSQL("DELETE FROM statistic");
            db.execSQL("DELETE FROM comment");
            db.execSQL("DELETE FROM photo");
            db.execSQL("DELETE FROM game");
            db.execSQL("DELETE FROM stamp");
            db.execSQL("DELETE FROM allstamps");
            db.execSQL("DELETE FROM user");
            db.execSQL("DELETE FROM account");
*/
        }
    }

    public void Close() {

        if (db != null) db.close();
    }

    public SQLiteDatabase getDb() {

        try {
            if (db == null) Open();
        } catch (Throwable th) {}

        return db;
    }
}
