package com.alexandrealencar.videoclean.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexandrealencar.videoclean.database.QueryHistoryContract.QueryHistoryEntry;

public class VideoCleanDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VideoClean.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + QueryHistoryEntry.TABLE_NAME + " (" +
            QueryHistoryEntry._ID + " INTEGER PRIMARY KEY," +
            QueryHistoryEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            QueryHistoryEntry.COLUMN_NAME_LINK + " TEXT," +
            QueryHistoryEntry.COLUMN_NAME_DATE_UPDATE + " REAL)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + QueryHistoryContract.QueryHistoryEntry.TABLE_NAME;

    public VideoCleanDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}