package cz.kalina.stampie.data.dao.impl;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.database.Cursor;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.dao.intf.IStampDAO;
import cz.kalina.stampie.data.entities.Stamp;

public class StampDAO extends GenericDAO<Stamp> implements IStampDAO {

    @Override
    public Class<Stamp> getTypeOfEntity() {
        return Stamp.class;
    }

    @Override
    public Stamp createEmptyEntity() {
        return new Stamp();
    }

    @Override
    public Stamp copyCursorToEntity(Cursor cursor, Stamp entity, Database db) {
        return entity;
    }

    @Override
    public ContentValues copyEntityToCursor(Stamp entity, Database db) {
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("name", entity.getName());
        values.put("category", entity.getCategory());
        values.put("county", entity.getCounty());
        values.put("published", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(entity.getPublished().getTime()));
        values.put("getGpsPositionLat", entity.getGpsPositionLat());
        values.put("getGpsPositionLng", entity.getGpsPositionLng());
        return values;
    }

    @Override
    public String getEntityNameOfIdCollumn() {
        return "id";
    }

    @Override
    public String getEntityNameOfTable() {
        return "stamp";
    }

    @Override
    public String[] getEntityNamesOfColumns() {
        String[] columns = { "id", "name", "category", "county", "published", "gpsPositionLat", "gpsPositionLng" };
        return columns;
    }
}
