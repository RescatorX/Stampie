package cz.kalina.stampie.data.dao.intf;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import cz.kalina.stampie.data.entities.BaseEntity;
import cz.kalina.stampie.utils.STPException;
import cz.kalina.stampie.data.Database;

public interface IGenericDAO<T extends BaseEntity> {

    /**
     * Find a POJO specified by its unique id.
     *
     * @param id Value for the Id.
     * @param db Database object.
     * @return Found POJO or null.
     */
    public T findById(String id, Database db) throws STPException;

    /**
     * Find all items.
     *
     * @param db Database object.
     * @return List of POJOs. This list is never null (but can be empty).
     */
    public List<T> findAll(Database db) throws STPException;

    /**
     * Find documents according to the specified criteria.
     *
     * @param offset Offset from which data rows are returned. Used for paging.
     * @param limit Maximal number of returned rows. Used for paging.
     * @param filter Serves to filter returned data.
     * @param sort Sorts returned data- (minus) means descending, + or missing ascending.
     * @param db Database object.
     * @return List of found documents. If nothing is found, it returns an empty list.
     */
    public List<T> find(int offset, int limit, String filter, String sort, Database db) throws STPException;

    /**
     * Create an item using data in the given POJO.
     *
     * @param entity Data to be used to create an item.
     * @param db Database object.
     * @return POJO data including UUID (if it is a part of POJO).
     */
    public T create(T entity, Database db) throws STPException;

    /**
     * Update an item using data in the given POJO.
     *
     * @param id Unique ID of the pojo.
     * @param entity Data to be used to update an item.
     * @param db Database object.
     * @return Data of the updated item.
     */
    public T update(String id, T entity, Database db) throws STPException;

    /**
     * Delete an item using data in the given POJO.
     *
     * @param id ID of the item to be deleted.
     * @param db Database object.
     * @return true in case the deleting was successful, false otherwise.
     */
    public boolean delete(String id, Database db) throws STPException;

    /**
     * Getting the class to identify the type of the entity.
     *
     * @return Type of the entity at runtime.
     */
    public Class<T> getTypeOfEntity();

    /**
     * Sets POJO members according to the given entity.
     *
     * @param cursor Cursor which values are to be used.
     * @param entity Target POJO.
     * @param db Database object.
     * @return POJO with values of the given entity.
     */
    public T copyCursorToEntity(Cursor cursor, T entity, Database db);

    /**
     * Sets POJO members according to the given entity.
     *
     * @param entity Target POJO.
     * @param db Database object.
     * @return POJO with values of the given entity.
     */
    public ContentValues copyEntityToCursor(T entity, Database db);

    /**
     * Creates an empty entity by its parameterless constructor.
     *
     * @return Created entity.
     */
    public T createEmptyEntity();

    /**
     * Name of the column which is used as ID-column
     *
     * @return Name of the column.
     */
    public String getEntityNameOfIdCollumn();

    /**
     * Name of the table which is used to access
     *
     * @return Name of the table.
     */
    public String getEntityNameOfTable();

    /**
     * Name of columns which is used to access
     *
     * @return Name of columns.
     */
    public String[] getEntityNamesOfColumns();
}