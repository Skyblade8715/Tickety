package com.sky.tickety.backend;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBAdapter";
    public static final String DATABASE_NAME = "TicketyDB";
    public static final int DATABASE_VERSION = 1;

    //Basic column for all tables
    public static final String KEY_ROWID = "_id";
    //List of all table's names
    public static final String TABLE_STATIONS_NAMES = "Station_names";
    public static final String TABLE_STATIONS_DATE = "Table_dates";


    //List of columns for TABLE_STATIONS_NAMES
    public static final String KEY_NAME = "Station_names";

    //List of columns for TABLE_STATIONS_DATE
    public static final String KEY_TABLE_NAMES = "Table_names";
    public static final String KEY_DATE = "Table_date";

    public static final String DATABASE_CREATE_TABLE_NAMES =
            "CREATE TABLE " + TABLE_STATIONS_NAMES
                    + " (" + KEY_ROWID + " INT PRIMARY KEY, "
                    + KEY_NAME + " TEXT NOT NULL);";


    public static final String DATABASE_CREATE_TABLE_STATIONS_DATE =
            "CREATE TABLE " + TABLE_STATIONS_DATE
                    + " (" + KEY_ROWID + " INT PRIMARY KEY, "
                    + KEY_TABLE_NAMES + " TEXT NOT NULL, "
                    + KEY_DATE + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {;
        _db.execSQL(DATABASE_CREATE_TABLE_NAMES);
        _db.execSQL(DATABASE_CREATE_TABLE_STATIONS_DATE);
//        _db.execSQL(DATABASE_CREATE_TABLE_KODY);
//        for (int i = 22; i < DATABASE_VERSION; i++) {
//            updateMyDatabase(_db, i, DATABASE_VERSION);
//        }
    }

    public void updateMyDatabase(SQLiteDatabase _db, int oldVersion, int newVersion) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < DATABASE_VERSION; i++) {
            updateMyDatabase(_db, i, DATABASE_VERSION);
        }
    }
}
