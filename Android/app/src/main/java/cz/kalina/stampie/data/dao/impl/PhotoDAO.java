package cz.kalina.stampie.data.dao.impl;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.database.Cursor;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.dao.intf.IPhotoDAO;
import cz.kalina.stampie.data.entities.Photo;

public class PhotoDAO extends GenericDAO<Photo> implements IPhotoDAO {

    @Override
    public Class<Photo> getTypeOfEntity() {
        return Photo.class;
    }

    @Override
    public Photo createEmptyEntity() {
        return new Photo();
    }

    @Override
    public Photo copyCursorToEntity(Cursor cursor, Photo entity, Database db) {
        return entity;
    }

    @Override
    public ContentValues copyEntityToCursor(Photo entity, Database db) {
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("name", entity.getName());
        values.put("description", entity.getDescription());
        values.put("content", entity.getContent());
        values.put("getGpsPositionLat", entity.getGpsPositionLat());
        values.put("getGpsPositionLng", entity.getGpsPositionLng());
        values.put("creator", entity.getCreator().getId());
        values.put("created", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(entity.getCreated().getTime()));
        return values;
    }

    @Override
    public String getEntityNameOfIdCollumn() {
        return "id";
    }

    @Override
    public String getEntityNameOfTable() {
        return "photo";
    }

    @Override
    public String[] getEntityNamesOfColumns() {
        String[] columns = { "id", "name", "description", "content", "gpsPositionLat", "gpsPositionLng", "creator", "created" };
        return columns;
    }
}
