package com.alexandrealencar.videoclean.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alexandrealencar.videoclean.database.QueryHistoryContract.QueryHistoryEntry;
import com.alexandrealencar.videoclean.entities.QueryHistory;

public final class VideoCleanController {
    private SQLiteDatabase db;

    public VideoCleanController(Context context) {
        db = new VideoCleanDB(context).getWritableDatabase();
    }

    public Cursor select() {
        String[] columns = {QueryHistoryEntry._ID, QueryHistoryEntry.COLUMN_NAME_LINK, QueryHistoryEntry.COLUMN_NAME_DESCRIPTION};
        return db.query(QueryHistoryEntry.TABLE_NAME, columns, null, null, null, null, QueryHistoryEntry.COLUMN_NAME_DATE_UPDATE);
    }

    public long insert(QueryHistory queryHistory) {
        ContentValues values = new ContentValues();
        values.put(QueryHistoryEntry.COLUMN_NAME_DESCRIPTION, queryHistory.getDescription());
        values.put(QueryHistoryEntry.COLUMN_NAME_LINK, queryHistory.getLink());
        values.put(QueryHistoryEntry.COLUMN_NAME_DATE_UPDATE, queryHistory.getDateUpdate());
        return db.insert(QueryHistoryEntry.TABLE_NAME, null, values);
    }

    public long delete(QueryHistory queryHistory) {
        String[] selectionArgs = { queryHistory.getId().toString() };
        return db.delete(QueryHistoryEntry.TABLE_NAME, QueryHistoryEntry._ID, selectionArgs);
    }

    public int update(QueryHistory queryHistory){
        ContentValues values = new ContentValues();
        values.put(QueryHistoryEntry.COLUMN_NAME_DESCRIPTION, queryHistory.getDescription());
        values.put(QueryHistoryEntry.COLUMN_NAME_LINK, queryHistory.getLink());
        values.put(QueryHistoryEntry.COLUMN_NAME_DATE_UPDATE, queryHistory.getDateUpdate());
        String[] selectionArgs = { queryHistory.getId().toString() };
        return db.update(QueryHistoryEntry.TABLE_NAME, values, QueryHistoryEntry._ID, selectionArgs);
    }

}
