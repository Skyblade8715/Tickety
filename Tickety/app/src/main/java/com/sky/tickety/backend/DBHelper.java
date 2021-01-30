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
    public static final String TABLE_STOPS_NAMES = "Stops_names";
    public static final String TABLE_TICKETS = "Tickets";
    public static final String TABLE_MPK_TICKETS = "mpkTickets";

    //List of columns for TABLE_STATIONS_NAMES
    public static final String KEY_NAME = "Station_names";

    //List of columns for TABLE_STOPS_NAMES
    public static final String KEY_STOP_NAME = "stopName";

    //List of columns for TABLE_MPK_TICKETS
    public static final String KEY_MPK_TICKET_NAME = "mpkTicketName";
    public static final String KEY_DURATION = "mpkTicketDuration";
    public static final String KEY_MPK_START_TIME = "mpkStartTime";
    public static final String KEY_MPK_TICKET_BARCODE = "mpkBarcode";
    public static final String KEY_MPK_TICKET_DISCOUNT_NAME = "mpkDiscountName";

    //List of columns for TABLE_TICKETS
    public static final String KEY_TICKET_NAME = "name";
    public static final String KEY_TICKET_SURNAME = "surname";
    public static final String KEY_TICKET_PROVIDER = "provider";
    public static final String KEY_TICKET_ID = "trainID";
    public static final String KEY_TICKET_START_TIME = "startTime";
    public static final String KEY_TICKET_END_TIME = "endTime";
    public static final String KEY_TICKET_CHOSEN_TIME = "chosenStartTime";
    public static final String KEY_TICKET_START_STATION = "startStation";
    public static final String KEY_TICKET_END_STATION = "endStation";
    public static final String KEY_TICKET_DISCOUNT_NAME = "discountName";
    public static final String KEY_TICKET_PRICE = "price";
    public static final String KEY_TICKET_BIKE = "bike";
    public static final String KEY_TICKET_DOG = "dog";
    public static final String KEY_TICKET_BAG = "bag";
    public static final String KEY_TICKET_BARCODE = "barcode";


    public static final String DATABASE_CREATE_TABLE_NAMES =
            "CREATE TABLE " + TABLE_STATIONS_NAMES
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL);";


    public static final String DATABASE_CREATE_TABLE_TICKETS =
            "CREATE TABLE " + TABLE_TICKETS
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                     + KEY_TICKET_NAME + " TEXT NOT NULL,"
                     + KEY_TICKET_SURNAME + " TEXT NOT NULL,"
                     + KEY_TICKET_PROVIDER + " TEXT NOT NULL,"
                     + KEY_TICKET_ID + " TEXT NOT NULL,"
                     + KEY_TICKET_START_TIME + " TEXT NOT NULL,"
                     + KEY_TICKET_END_TIME + " TEXT NOT NULL,"
                     + KEY_TICKET_CHOSEN_TIME + " TEXT NOT NULL,"
                     + KEY_TICKET_START_STATION + " TEXT NOT NULL,"
                     + KEY_TICKET_END_STATION + " TEXT NOT NULL,"
                     + KEY_TICKET_DISCOUNT_NAME + " TEXT NOT NULL,"
                     + KEY_TICKET_BIKE + " INTEGER NOT NULL,"
                     + KEY_TICKET_DOG + " INTEGER NOT NULL,"
                     + KEY_TICKET_BAG + " INTEGER NOT NULL,"
                     + KEY_TICKET_PRICE + " INTEGER NOT NULL,"
                     + KEY_TICKET_BARCODE + " TEXT NOT NULL);";

    public static final String DATABASE_CREATE_TABLE_STOPS_NAMES =
            "CREATE TABLE " + TABLE_STOPS_NAMES
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_STOP_NAME + " TEXT NOT NULL);";

    public static final String DATABASE_CREATE_MPK_TICKET_TABLE =
            "CREATE TABLE " + TABLE_MPK_TICKETS
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_MPK_TICKET_NAME + " TEXT NOT NULL,"
                    + KEY_DURATION + " TEXT NOT NULL,"
                    + KEY_MPK_START_TIME + " TEXT,"
                    + KEY_MPK_TICKET_BARCODE + " TEXT NOT NULL,"
                    + KEY_MPK_TICKET_DISCOUNT_NAME + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {;
        _db.execSQL(DATABASE_CREATE_TABLE_NAMES);
        _db.execSQL(DATABASE_CREATE_TABLE_TICKETS);
        _db.execSQL(DATABASE_CREATE_TABLE_STOPS_NAMES);
        _db.execSQL(DATABASE_CREATE_MPK_TICKET_TABLE);
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
