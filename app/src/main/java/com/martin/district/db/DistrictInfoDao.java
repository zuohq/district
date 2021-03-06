package com.martin.district.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.martin.district.db.DistrictInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "district".
*/
public class DistrictInfoDao extends AbstractDao<DistrictInfo, Long> {

    public static final String TABLENAME = "district";

    /**
     * Properties of entity DistrictInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property AdCode = new Property(0, long.class, "adCode", true, "AD_CODE");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property CityCode = new Property(2, int.class, "cityCode", false, "CITY_CODE");
    };


    public DistrictInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DistrictInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"district\" (" + //
                "\"AD_CODE\" INTEGER PRIMARY KEY NOT NULL ," + // 0: adCode
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"CITY_CODE\" INTEGER NOT NULL );"); // 2: cityCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"district\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DistrictInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getAdCode());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getCityCode());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DistrictInfo readEntity(Cursor cursor, int offset) {
        DistrictInfo entity = new DistrictInfo( //
            cursor.getLong(offset + 0), // adCode
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2) // cityCode
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DistrictInfo entity, int offset) {
        entity.setAdCode(cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setCityCode(cursor.getInt(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DistrictInfo entity, long rowId) {
        entity.setAdCode(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DistrictInfo entity) {
        if(entity != null) {
            return entity.getAdCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
