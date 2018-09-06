package cz.kalina.stampie.data.dao.impl;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.database.Cursor;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.dao.intf.IExtDataDAO;
import cz.kalina.stampie.data.entities.ExtData;
import cz.kalina.stampie.utils.STPException;

public class ExtDataDAO extends GenericDAO<ExtData> implements IExtDataDAO {

    @Override
    public Class<ExtData> getTypeOfEntity() {
        return ExtData.class;
    }

    @Override
    public ExtData createEmptyEntity() {
        return new ExtData();
    }

    @Override
    public ExtData copyCursorToEntity(Cursor cursor, ExtData entity, Database db) {
        return entity;
    }

    @Override
    public ContentValues copyEntityToCursor(ExtData entity, Database db) {
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("text1", entity.getText1());
        values.put("text2", entity.getText2());
        values.put("text3", entity.getText3());
        values.put("text4", entity.getText4());
        values.put("created", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(entity.getCreated().getTime()));
        return values;
    }

    @Override
    public String getEntityNameOfIdCollumn() {
        return "id";
    }

    @Override
    public String getEntityNameOfTable() {
        return "extdata";
    }

    @Override
    public String[] getEntityNamesOfColumns() {
        String[] columns = { "id", "text1", "text1", "text1", "text1", "created" };
        return columns;
    }

    @Override
    public ExtData getItemByType(String type, Database db) throws STPException {

        ExtData item = null;
        Cursor cursor = null;

        try {

            cursor = db.getDb().query(true, getEntityNameOfTable(),
                    getEntityNamesOfColumns(),
                    "type = " + type, null, null, null, null, null);

            if (cursor == null) throw new STPException("Query error while loading entity");

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                if (cursor.moveToNext()) {
                    item = copyCursorToEntity(cursor, createEmptyEntity(), db);
                }
            }

        } catch (Exception ex) {
            throw new STPException("Entity load by type error: " + ex.getMessage(), ex);
        } finally {
            if (cursor != null) cursor.close();
        }

        return item;
    }
}
