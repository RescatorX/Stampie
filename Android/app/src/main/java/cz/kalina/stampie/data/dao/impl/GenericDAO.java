package cz.kalina.stampie.data.dao.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.dao.intf.IGenericDAO;
import cz.kalina.stampie.data.entities.BaseEntity;
import cz.kalina.stampie.utils.STPException;

public abstract class GenericDAO<T extends BaseEntity> implements IGenericDAO<T> {

    @Override
    public T findById(long id, Database db) throws STPException {

        T foundPojo = null;
        Cursor cursor = null;

        try {

            cursor = db.getDb().query(true, getEntityNameOfTable(),
                    getEntityNamesOfColumns(),
                    getEntityNameOfIdCollumn() + " = " + id, null, null, null, null, null);

            if (cursor == null) throw new STPException("Query error while loading entity");

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                if (cursor.moveToNext()) {
                    foundPojo = copyCursorToEntity(cursor, createEmptyEntity(), db);
                }
            }

        } catch (Exception ex) {
            throw new STPException("Entity load by ID error: " + ex.getMessage(), ex);
        } finally {
            if (cursor != null) cursor.close();
        }

        return foundPojo;
    }

    @Override
    public List<T> findAll(Database db) throws STPException {

        List<T> foundEntities = new ArrayList<>();
        Cursor cursor = null;

        try {

            cursor = db.getDb().query(true, getEntityNameOfTable(),
                    getEntityNamesOfColumns(),
                    null, null, null, null, null, null);

            if (cursor == null) throw new STPException("Query error while loading entity");

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                while (cursor.moveToNext()) {
                    foundEntities.add(copyCursorToEntity(cursor, createEmptyEntity(), db));
                }
            }

        } catch (Exception ex) {
            throw new STPException("Entities list load error: " + ex.getMessage(), ex);
        } finally {
            if (cursor != null) cursor.close();
        }

        return foundEntities;
    }

    @Override
    public T create(T entity, Database db) throws STPException {

        long newId = db.getDb().insert(getEntityNameOfTable(), null, copyEntityToCursor(entity, db));
        entity.setId(newId);

        return entity;
    }

    @Override
    public T update(long id, T entity, Database db) throws STPException {

        db.getDb().update(getEntityNameOfTable(), copyEntityToCursor(entity, db), getEntityNameOfIdCollumn() + " = " + id, null);

        return entity;
    }

    @Override
    public boolean delete(long id, Database db) throws STPException {

        int rows = db.getDb().delete(getEntityNameOfTable(), getEntityNameOfIdCollumn() + " = " + id, null);

        return (rows > 0);
    }

    @Override
    public List<T> find(int offset, int limit, String filter, String sort, Database db) throws STPException {

        List<T> foundEntities = new ArrayList<>();
        Cursor cursor = null;

        try {

            cursor = db.getDb().rawQuery(getItemQuery(filter, sort), getEntityNamesOfColumns());

            if (cursor == null) throw new STPException("Query error while loading filtered entities");

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                while (cursor.moveToNext()) {
                    foundEntities.add(copyCursorToEntity(cursor, createEmptyEntity(), db));
                }
            }

        } catch (Exception ex) {
            throw new STPException("Entities list load error: " + ex.getMessage(), ex);
        } finally {
            if (cursor != null) cursor.close();
        }

        return foundEntities;
    }

    /**
     * Creates a query string according to the given filter and sort.
     *
     * @param filter Serves to filter returned data.
     * @param sort Sorts returned data- (minus) means descending, + or missing ascending.
     * @return Query string
     */
    private String getItemQuery(String filter, String sort) {
        return "from " + getTypeOfEntity().getSimpleName()
                + ( filter != null ?(" " + filter) : "")
                + ( sort != null ? ( " " + sort) : "");
    }
}
