package com.example.alexe.affiche;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "affiche";
    public static final String TABLE_NAME = "cinema";

    public static final String KEY_TITLE = "title";
    public static final String KEY_INFO =   "info";
    public static final String KEY_IMG = "img";
    public static final String KEY_YEAR =   "year";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_ADDRESS = "address";

    Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.w("SQLite", "Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_TITLE
                + " text primary key," + KEY_INFO + " text," + KEY_IMG + " text," + KEY_YEAR + " text," + KEY_DURATION + " text," + KEY_ADDRESS + " text" + ")");
        Log.w("SQLite", "Creating");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
        Log.w("SQLite", "Upgrading");
    }
}
