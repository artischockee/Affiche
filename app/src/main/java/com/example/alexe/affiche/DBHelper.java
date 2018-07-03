package com.example.alexe.affiche;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "affiche";

    //TABLE_NAMES
    public static final String TABLE_NAME = "cinema";
    public static final String TABLE_NAME_1 = "theater";
    public static final String TABLE_NAME_2 = "children";
    public static final String TABLE_NAME_3 = "events";
    public static final String TABLE_NAME_4 = "favorites";

    //TABLE_CINEMA_COLUMNS
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_INFO =   "info";
    public static final String KEY_IMG = "img";
    public static final String KEY_YEAR =   "year";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_ADDRESS = "address";

    //TABLE_THEATER & TABLE_CHILDREN & TABLE_EVENT COLUMNS
    public static final String KEY_TITLE_1 = "title";
    public static final String KEY_TIME_1 =   "time";
    public static final String KEY_DATE_1 = "date";
    public static final String KEY_IMG_1 =   "img";
    public static final String KEY_PLACE_1 = "place";
    public static final String KEY_ADDRESS_1 = "address";
    public static final String KEY_ID_1 = "id";

    //TABLE_FAVORITES
    public static final String KEY_ID_2 = "id";
    public static final String KEY_CATEGORY_2 = "category";
    public static final String KEY_TITLE_2 = "title";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.w("SQLite", "Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //ТАБЛИЦА CINEMA
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID + " integer primary key," + KEY_TITLE
                + " text," + KEY_INFO + " text," + KEY_IMG + " text," + KEY_YEAR + " text," + KEY_DURATION + " text," + KEY_ADDRESS + " text" + ")");

        //ТАБЛИЦА ТЕАТР
        db.execSQL("create table " + TABLE_NAME_1 + "(" + KEY_ID_1 + " integer primary key," + KEY_TITLE_1
                + " text," + KEY_TIME_1 + " text," + KEY_DATE_1 + " text," + KEY_IMG_1 + " text," + KEY_PLACE_1 + " text," + KEY_ADDRESS_1 + " text" + ")");
        Log.w("SQLite", "Creating");

        //ТАБЛИЦА ДЕТИ
        db.execSQL("create table " + TABLE_NAME_2 + "(" + KEY_ID_1 + " integer primary key, " + KEY_TITLE_1
                + " text," + KEY_TIME_1 + " text," + KEY_DATE_1 + " text," + KEY_IMG_1 + " text," + KEY_PLACE_1 + " text," + KEY_ADDRESS_1 + " text" + ")");
        Log.w("SQLite", "Creating");

        //ТАБЛИЦА СОБЫТИЯ
        db.execSQL("create table " + TABLE_NAME_3 + "(" + KEY_ID_1 + " integer primary key," + KEY_TITLE_1
                + " text," + KEY_TIME_1 + " text," + KEY_DATE_1 + " text," + KEY_IMG_1 + " text," + KEY_PLACE_1 + " text," + KEY_ADDRESS_1 + " text" + ")");

        //ТАБЛИЦА ИЗБРАННОЕ
        db.execSQL("create table " + TABLE_NAME_4 + "(" + KEY_ID_2 + " integer primary key," + KEY_CATEGORY_2 + " text," + KEY_TITLE_2 + " text" + ")");

        Log.w("SQLite", "Creating");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("drop table if exists " + TABLE_NAME_1);
        db.execSQL("drop table if exists " + TABLE_NAME_2);
        db.execSQL("drop table if exists " + TABLE_NAME_3);
        db.execSQL("drop table if exists " + TABLE_NAME_4);
        onCreate(db);
        Log.w("SQLite", "Upgrading");
    }
}