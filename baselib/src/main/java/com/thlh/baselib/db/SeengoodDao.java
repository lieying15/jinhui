package com.thlh.baselib.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.thlh.baselib.model.GoodsDb;

import com.thlh.baselib.model.Seengood;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SEENGOOD".
*/
public class SeengoodDao extends AbstractDao<Seengood, Long> {

    public static final String TABLENAME = "SEENGOOD";

    /**
     * Properties of entity Seengood.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Seengood_id = new Property(0, Long.class, "seengood_id", true, "_id");
        public final static Property Goods_id = new Property(1, long.class, "goods_id", false, "GOODS_ID");
    }

    private DaoSession daoSession;


    public SeengoodDao(DaoConfig config) {
        super(config);
    }
    
    public SeengoodDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEENGOOD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: seengood_id
                "\"GOODS_ID\" INTEGER NOT NULL );"); // 1: goods_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEENGOOD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Seengood entity) {
        stmt.clearBindings();
 
        Long seengood_id = entity.getSeengood_id();
        if (seengood_id != null) {
            stmt.bindLong(1, seengood_id);
        }
        stmt.bindLong(2, entity.getGoods_id());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Seengood entity) {
        stmt.clearBindings();
 
        Long seengood_id = entity.getSeengood_id();
        if (seengood_id != null) {
            stmt.bindLong(1, seengood_id);
        }
        stmt.bindLong(2, entity.getGoods_id());
    }

    @Override
    protected final void attachEntity(Seengood entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Seengood readEntity(Cursor cursor, int offset) {
        Seengood entity = new Seengood( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // seengood_id
            cursor.getLong(offset + 1) // goods_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Seengood entity, int offset) {
        entity.setSeengood_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGoods_id(cursor.getLong(offset + 1));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Seengood entity, long rowId) {
        entity.setSeengood_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Seengood entity) {
        if(entity != null) {
            return entity.getSeengood_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Seengood entity) {
        return entity.getSeengood_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getGoodsDbDao().getAllColumns());
            builder.append(" FROM SEENGOOD T");
            builder.append(" LEFT JOIN GOODS_DB T0 ON T.\"GOODS_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Seengood loadCurrentDeep(Cursor cursor, boolean lock) {
        Seengood entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        GoodsDb goodsdb = loadCurrentOther(daoSession.getGoodsDbDao(), cursor, offset);
         if(goodsdb != null) {
            entity.setGoodsdb(goodsdb);
        }

        return entity;    
    }

    public Seengood loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Seengood> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Seengood> list = new ArrayList<Seengood>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Seengood> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Seengood> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
