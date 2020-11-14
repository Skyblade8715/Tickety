package com.sky.tickety.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter extends DBHelper {

    private final Context context;
    private SQLiteDatabase db;
    private DBAdapter instance;

    public DBAdapter getInstance(Context ctx){
        if(instance == null){
            instance = new DBAdapter(ctx);
        }
        return instance;
    }

    public DBAdapter(Context ctx) {
        super(ctx);
        this.open();
        this.context = ctx;
    }

    public void open() {
        db = super.getWritableDatabase();
    }

    public void close() {
        super.close();
    }



}