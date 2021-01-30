package com.sky.tickety.backend;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBAdapter extends DBHelper {

    private final Context context;
    private SQLiteDatabase db;
    private static DBAdapter instance;

    public static DBAdapter getInstance(Context ctx){
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

    public void insert_Ticket(String name, String surname, String provider,
                              String trainID, String startTime, String endTime,
                              String chosenStartTime, String startStation,
                              String endStation, String discountName,
                              double price, int bike, int dog, int bag, String barcode) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TICKET_NAME, name);
        initialValues.put(KEY_TICKET_SURNAME, surname);
        initialValues.put(KEY_TICKET_PROVIDER, provider);
        initialValues.put(KEY_TICKET_ID, trainID);
        initialValues.put(KEY_TICKET_START_TIME, chosenStartTime.split(" ")[0] + " " + startTime);
        initialValues.put(KEY_TICKET_END_TIME, chosenStartTime.split(" ")[0] + " " + endTime);
        initialValues.put(KEY_TICKET_CHOSEN_TIME, chosenStartTime);
        initialValues.put(KEY_TICKET_START_STATION, startStation);
        initialValues.put(KEY_TICKET_END_STATION, endStation);
        initialValues.put(KEY_TICKET_DISCOUNT_NAME, discountName);
        initialValues.put(KEY_TICKET_PRICE, price);
        initialValues.put(KEY_TICKET_BIKE, bike);
        initialValues.put(KEY_TICKET_DOG, dog);
        initialValues.put(KEY_TICKET_BAG, bag);
        initialValues.put(KEY_TICKET_BARCODE, barcode);
        db.insert(TABLE_TICKETS, null, initialValues);
    }

    public List<String> select_Ticket_ByIDAndProvider(String barcode, String provider){
        String sql = "SELECT * FROM " + TABLE_TICKETS + " WHERE " + KEY_TICKET_BARCODE
                + " ='" + barcode + "' and " + KEY_TICKET_PROVIDER + " ='" + provider + "'";

        List<String> result = new ArrayList<>();
        Cursor c;
        c = db.rawQuery(sql, null);;
        if(c.moveToFirst()){
            for(int i = 0; i < c.getCount(); i++){
                result.add(c.getString(0));
                result.add(c.getString(1));
                result.add(c.getString(2));
                result.add(c.getString(3));
                result.add(c.getString(4));
                result.add(c.getString(5));
                result.add(c.getString(6));
                result.add(c.getString(8));
                result.add(c.getString(9));
                result.add(c.getString(10));
                result.add(c.getString(11));
                result.add(c.getString(12));
                result.add(c.getString(13));
                result.add(c.getString(14));
                result.add(c.getString(15));
                c.moveToNext();
            }
        } else {
            result = null;
        }
        c.close();
        return result;

    }

    public void deleteFrom_Tickets(){
        String sql = "DELETE FROM " + TABLE_MPK_TICKETS;
        db.execSQL(sql);
    }

    public List<List<String>> select_Tickets() {
        String sql = "SELECT * FROM " + TABLE_TICKETS + " ORDER BY " + KEY_ROWID + " DESC";
        Cursor c;
        List<List<String>> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        boolean toAdd;
        c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            for(int i = 0; i < c.getCount(); i++){
                temp.add(c.getString(0));
                temp.add(c.getString(1));
                temp.add(c.getString(2));
                temp.add(c.getString(3));
                temp.add(c.getString(4));
                temp.add(c.getString(5));
                temp.add(c.getString(6));
                temp.add(c.getString(8));
                temp.add(c.getString(9));
                temp.add(c.getString(10));
                temp.add(c.getString(11));
                temp.add(c.getString(12));
                temp.add(c.getString(13));
                temp.add(c.getString(14));
                temp.add(c.getString(15));
                String startTime = c.getString(6);
                if(startTime != null){
                    Date start = null;
                    try {
                        start = formatter.parse(startTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date valid = Calendar.getInstance().getTime();
                    toAdd = start.after(valid);
                } else {
                    toAdd = true;
                }
                if(toAdd) {
                    result.add(new ArrayList<>(temp));
                }
                temp.clear();
                c.moveToNext();
            }
        } else {
            result = null;
        }
        c.close();
        return result;
    }

    public void insert_MPK_Ticket(String ticketName, int duration, String startTime,
                                  String barcode) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MPK_TICKET_NAME, ticketName);
        initialValues.put(KEY_DURATION, duration);
        initialValues.put(KEY_MPK_START_TIME, startTime);
        initialValues.put(KEY_MPK_TICKET_BARCODE, barcode);
        initialValues.put(KEY_MPK_TICKET_DISCOUNT_NAME,
                context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE).
                        getString("MPK", "XX"));

        db.insert(TABLE_MPK_TICKETS, null, initialValues);
    }

    public List<String> select_MPK_Ticket_ByBarcode(String barcode){
        String sql = "SELECT * FROM " + TABLE_MPK_TICKETS + " WHERE " + KEY_MPK_TICKET_BARCODE
                + " ='" + barcode + "'";

        List<String> result = new ArrayList<>();
        Cursor c;
        c = db.rawQuery(sql, null);;
        if(c.moveToFirst()){
            for(int i = 0; i < c.getCount(); i++){
                result.add(c.getString(1));
                result.add(c.getString(2));
                result.add(c.getString(3));
                result.add(c.getString(4));
                result.add(c.getString(5));
                c.moveToNext();
            }
        } else {
            result = null;
        }
        c.close();
        return result;
    }

    public boolean update_MPKTicket(String barcode, String startTime) {
        String where = KEY_MPK_TICKET_BARCODE + "=?";
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_MPK_START_TIME, startTime);
        return db.update(TABLE_MPK_TICKETS, newValues, where, new String[]{barcode}) != 0;
    }

    public List<List<String>> select_MPK_Tickets() {
        String sql = "SELECT * FROM " + TABLE_MPK_TICKETS + " ORDER BY " + KEY_MPK_START_TIME + " DESC";
        Cursor c;
        List<List<String>> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        boolean toAdd;
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                temp.add(c.getString(1));
                String startTime = c.getString(3);
                if(startTime != null){
                    Date valid = Calendar.getInstance().getTime();
                    Date end = null;
                    try {
                        end = new Date(formatter.parse(startTime).getTime() + (60000 * Integer.parseInt(c.getString(2))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    toAdd = end.after(valid);
                } else {
                    toAdd = true;
                }
                temp.add(c.getString(2));
                temp.add(c.getString(3));
                temp.add(c.getString(4));
                temp.add(c.getString(5));
                if(toAdd) {
                    result.add(new ArrayList<>(temp));
                }
                temp.clear();
                c.moveToNext();
            }
        } else {
            result = null;
        }
        c.close();
        return result;
    }

    public void insert_Station_Names(ArrayList<String> names, String date) {
        String sql = "DELETE FROM " + TABLE_STATIONS_NAMES;
        db.execSQL(sql);
        ContentValues initialValues = new ContentValues();
        for(String name : names){
            initialValues.put(KEY_NAME, name);
            db.insert(TABLE_STATIONS_NAMES, null, initialValues);
        }
        SharedPreferences prefs = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        prefs.edit().putString("TABLE_STATIONS_NAMES", date).apply();
    }

    public ArrayList<String> select_Station_Names() {
        String sql = "SELECT " + KEY_NAME + " FROM " + TABLE_STATIONS_NAMES;
        Cursor c;
        ArrayList<String> temp = new ArrayList<>();
        c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            for(int i = 0; i < c.getCount(); i++){
                temp.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            temp = null;
        }
        c.close();
        return temp;
    }


    public void insert_Stops_Names(ArrayList<String> names, String date) {
        String sql = "DELETE FROM " + TABLE_STOPS_NAMES;
        db.execSQL(sql);
        ContentValues initialValues = new ContentValues();
        for(String name : names){
            initialValues.put(KEY_STOP_NAME, name);
            db.insert(TABLE_STOPS_NAMES, null, initialValues);
        }
        SharedPreferences prefs = context.getSharedPreferences("com.sky.tickety", Context.MODE_PRIVATE);
        prefs.edit().putString("TABLE_STOPS_NAMES", date).apply();
    }

    public ArrayList<String> select_Stops_Names() {
        String sql = "SELECT " + KEY_STOP_NAME + " FROM " + TABLE_STOPS_NAMES;
        Cursor c;
        ArrayList<String> temp = new ArrayList<>();
        c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            for(int i = 0; i < c.getCount(); i++){
                temp.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            temp = null;
        }
        c.close();
        return temp;
    }
}