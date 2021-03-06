package com.thlh.baselib.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.thlh.baselib.model.Supplier;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SUPPLIER".
*/
public class SupplierDao extends AbstractDao<Supplier, Long> {

    public static final String TABLENAME = "SUPPLIER";

    /**
     * Properties of entity Supplier.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Dbid = new Property(0, Long.class, "dbid", true, "_id");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Store_name = new Property(3, String.class, "store_name", false, "STORE_NAME");
        public final static Property Contact = new Property(4, String.class, "contact", false, "CONTACT");
        public final static Property Mobile = new Property(5, String.class, "mobile", false, "MOBILE");
        public final static Property Phone = new Property(6, String.class, "phone", false, "PHONE");
        public final static Property Email = new Property(7, String.class, "email", false, "EMAIL");
    }


    public SupplierDao(DaoConfig config) {
        super(config);
    }
    
    public SupplierDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SUPPLIER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: dbid
                "\"ID\" TEXT," + // 1: id
                "\"NAME\" TEXT," + // 2: name
                "\"STORE_NAME\" TEXT," + // 3: store_name
                "\"CONTACT\" TEXT," + // 4: contact
                "\"MOBILE\" TEXT," + // 5: mobile
                "\"PHONE\" TEXT," + // 6: phone
                "\"EMAIL\" TEXT);"); // 7: email
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SUPPLIER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Supplier entity) {
        stmt.clearBindings();
 
        Long dbid = entity.getDbid();
        if (dbid != null) {
            stmt.bindLong(1, dbid);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String store_name = entity.getStore_name();
        if (store_name != null) {
            stmt.bindString(4, store_name);
        }
 
        String contact = entity.getContact();
        if (contact != null) {
            stmt.bindString(5, contact);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(6, mobile);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(8, email);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Supplier entity) {
        stmt.clearBindings();
 
        Long dbid = entity.getDbid();
        if (dbid != null) {
            stmt.bindLong(1, dbid);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String store_name = entity.getStore_name();
        if (store_name != null) {
            stmt.bindString(4, store_name);
        }
 
        String contact = entity.getContact();
        if (contact != null) {
            stmt.bindString(5, contact);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(6, mobile);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(7, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(8, email);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Supplier readEntity(Cursor cursor, int offset) {
        Supplier entity = new Supplier( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // dbid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // store_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // contact
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mobile
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // phone
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // email
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Supplier entity, int offset) {
        entity.setDbid(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStore_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContact(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMobile(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPhone(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEmail(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Supplier entity, long rowId) {
        entity.setDbid(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Supplier entity) {
        if(entity != null) {
            return entity.getDbid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Supplier entity) {
        return entity.getDbid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
