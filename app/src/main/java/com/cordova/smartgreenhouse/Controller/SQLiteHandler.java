package com.cordova.smartgreenhouse.Controller;

/**
 * Created by root on 11/9/17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "android_api";
    private static final String TABLE_USER = "user";
    private static final String TABLE_TEMPERATURE = "temperature";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_HUMIDITY = "humidity";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
        String CREATE_TEMPERATURE_TABLE = "CREATE TABLE " + TABLE_TEMPERATURE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEMPERATURE + " TEXT,"
                + KEY_HUMIDITY + " TEXT " + ")";
        db.execSQL(CREATE_TEMPERATURE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String phone, String address,
                        String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);

        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    public void updateUser(String name, String phone, String address, String gender, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);



        long id = db.update(TABLE_USER,  values, KEY_EMAIL + "='" + email+"'", null);
        db.close();

        Log.d(TAG, "New user updated into sqlite: " + id);
    }

    public void addTumbuhan(String name, String email, String phone, String gender, String address,
                        String uid, String created_at, String pelajaran, String description,
                            String active, String work) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);



        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updateTumbuhan(String name, String phone, String address, String gender,
                               String email, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);

        long id = db.update(TABLE_USER,  values, KEY_EMAIL + "='" + email+"'", null);
        db.close();

        Log.d(TAG, "New user updated into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getTemperatureDetails() {
        HashMap<String, String> temperature = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPERATURE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            temperature.put("temperature", cursor.getString(1));
            temperature.put("humidity", cursor.getString(2));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching temperature from Sqlite: " + temperature.toString());

        return temperature;
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void addTemperature(String temperature, String humidity, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE, temperature);
        values.put(KEY_HUMIDITY, humidity);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(TABLE_TEMPERATURE, null, values);
        db.close();

        Log.d(TAG, "New temperature inserted into sqlite: " + id);
    }
}