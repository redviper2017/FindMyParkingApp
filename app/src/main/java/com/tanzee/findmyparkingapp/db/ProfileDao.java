package com.tanzee.findmyparkingapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "PROFILE".
*/
public class ProfileDao extends AbstractDao<Profile, Long> {

    public static final String TABLENAME = "PROFILE";

    /**
     * Properties of entity Profile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Email = new Property(1, String.class, "email", false, "EMAIL");
        public final static Property Image = new Property(2, String.class, "image", false, "IMAGE");
        public final static Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public final static Property Phone = new Property(4, String.class, "phone", false, "PHONE");
        public final static Property Name = new Property(5, String.class, "name", false, "NAME");
        public final static Property Reg = new Property(6, String.class, "reg", false, "REG");
        public final static Property Lic = new Property(7, String.class, "lic", false, "LIC");
        public final static Property Uid = new Property(8, String.class, "uid", false, "UID");
    }


    public ProfileDao(DaoConfig config) {
        super(config);
    }
    
    public ProfileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROFILE\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EMAIL\" TEXT UNIQUE ," + // 1: email
                "\"IMAGE\" TEXT," + // 2: image
                "\"PASSWORD\" TEXT," + // 3: password
                "\"PHONE\" TEXT UNIQUE ," + // 4: phone
                "\"NAME\" TEXT," + // 5: name
                "\"REG\" TEXT UNIQUE ," + // 6: reg
                "\"LIC\" TEXT UNIQUE ," + // 7: lic
                "\"UID\" TEXT UNIQUE );"); // 8: uid
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROFILE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Profile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(6, name);
        }
 
        String reg = entity.getReg();
        if (reg != null) {
            stmt.bindString(7, reg);
        }
 
        String lic = entity.getLic();
        if (lic != null) {
            stmt.bindString(8, lic);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(9, uid);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Profile entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(6, name);
        }
 
        String reg = entity.getReg();
        if (reg != null) {
            stmt.bindString(7, reg);
        }
 
        String lic = entity.getLic();
        if (lic != null) {
            stmt.bindString(8, lic);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(9, uid);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Profile readEntity(Cursor cursor, int offset) {
        Profile entity = new Profile( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // image
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // password
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // phone
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // name
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // reg
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // lic
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // uid
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Profile entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImage(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPassword(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPhone(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setReg(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLic(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUid(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Profile entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Profile entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Profile entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
