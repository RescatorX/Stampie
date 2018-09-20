package cz.kalina.stampie.data.dao;

import android.util.Log;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.dao.impl.ExtDataDAO;
import cz.kalina.stampie.data.dao.impl.PhotoDAO;
import cz.kalina.stampie.data.dao.impl.StampDAO;
import cz.kalina.stampie.data.dao.intf.IExtDataDAO;
import cz.kalina.stampie.data.dao.intf.IPhotoDAO;
import cz.kalina.stampie.data.dao.intf.IStampDAO;

public class DAOFactory {

    /**
     * Singleton to force just once instance across the system.
     */
    private static DAOFactory INSTANCE;

    /**
     * Database access object.
     */
    private Database db = null;

    /**
     * Returns a true when DAO Factory is ready to be used.
     */
    private static boolean ready = false;

    /**
     * Factory method to get the INSTANCE.
     *
     * @return Singleton instance of this class
     */
    public static DAOFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DAOFactory();
            ready = true;
        }
        return INSTANCE;
    }

    /**
     * Hidden constructor to create the INSTANCE.
     */
    private DAOFactory() {
        Log.d(DAOFactory.class.getName(), "constructor: ENTRY");
        db = MainActivity.getDb();
        Log.d(DAOFactory.class.getName(), "constructor: EXIT DAO Factory ready");
    }

    /**
     * Close the DAO factory.
     */
    public void close() {
        Log.d(DAOFactory.class.getName(), "close: ENTRY");
        try {
            MainActivity.CloseDb();
            ready = false;
            INSTANCE = null;
            Log.d(DAOFactory.class.getName(), "close: EXIT ");
        } catch (Exception exc) {
            Log.e(DAOFactory.class.getName(), "close: EXIT Cannot close due to the exception: " + exc.getMessage(), exc);
        }
    }

    /**
     * Returns a true when DAO Factory is ready to be used.
     *
     * @return true when DAO Factory is ready to be used.
     */
    public static boolean isReady() {
        return ready;
    }

    /**
     * Factory method to provide an instance of the IExtDataDAO.
     *
     * @return IExtDataDAO to access the table with external data.
     */
    public IExtDataDAO getExtDataDAO() {
        return new ExtDataDAO();
    }

    /**
     * Factory method to provide an instance of the IPhotoDAO.
     *
     * @return IPhotoDAO to access the table with photos.
     */
    public IPhotoDAO getPhotoDAO() {
        return new PhotoDAO();
    }

    /**
     * Factory method to provide an instance of the IStampDAO.
     *
     * @return IStampDAO to access the table with stamps.
     */
    public IStampDAO getStampDAO() {
        return new StampDAO();
    }
}
