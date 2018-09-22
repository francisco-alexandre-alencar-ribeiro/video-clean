package com.alexandrealencar.videoclean.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alexandrealencar.videoclean.database.QueryContract.QueryEntry;
import com.alexandrealencar.videoclean.entities.QueryHistory;

public final class VideoCleanController {
    private SQLiteDatabase db;

    public VideoCleanController(Context context) {
        db = new VideoCleanDB(context).getWritableDatabase();
    }

    public Cursor select( String selection , String[] selectionArqs ) {
        String[] columns = {QueryEntry._ID, QueryEntry.COLUMN_NAME_LINK, QueryEntry.COLUMN_NAME_DESCRIPTION , QueryEntry.COLUMN_NAME_IS_FAVORITE , QueryEntry.COLUMN_NAME_CURRENT_POSITION};
        return db.query(QueryEntry.TABLE_NAME, columns, selection, selectionArqs, null, null, QueryEntry._ID );
    }

    public Cursor selectFavorite() {
        String[] columns = {QueryEntry._ID, QueryEntry.COLUMN_NAME_LINK, QueryEntry.COLUMN_NAME_DESCRIPTION};
        return db.query(QueryEntry.TABLE_NAME, columns, QueryEntry.COLUMN_NAME_IS_FAVORITE + " = ? ", new String[]{ "1" } , null, null, QueryEntry._ID );
    }

    public Cursor selectHistory() {
        String[] columns = {QueryEntry._ID, QueryEntry.COLUMN_NAME_LINK, QueryEntry.COLUMN_NAME_DESCRIPTION};
        return db.query(QueryEntry.TABLE_NAME, columns, QueryEntry.COLUMN_NAME_VISUALIZED + " = ? ", new String[]{ "1" } , null, null, QueryEntry.COLUMN_NAME_DATE_UPDATE + " DESC" );
    }

    public long insert(QueryHistory queryHistory) {
        ContentValues values = new ContentValues();
        values.put(QueryEntry.COLUMN_NAME_DESCRIPTION, queryHistory.getDescription());
        values.put(QueryEntry.COLUMN_NAME_LINK, queryHistory.getLink());
        values.put(QueryEntry.COLUMN_NAME_DATE_UPDATE, queryHistory.getDateUpdate());
        values.put(QueryEntry.COLUMN_NAME_DATE_CREATE, queryHistory.getDateCreate());
        values.put(QueryEntry.COLUMN_NAME_VISUALIZED, queryHistory.getVisualized());
        values.put(QueryEntry.COLUMN_NAME_IS_FAVORITE, queryHistory.getIsFavorite());
        values.put(QueryEntry.COLUMN_NAME_CURRENT_POSITION, queryHistory.getCurrentPosition());
        return db.insert(QueryEntry.TABLE_NAME, null, values);
    }

    public long delete(QueryHistory queryHistory) {
        String[] selectionArgs = { queryHistory.getId().toString() };
        return db.delete(QueryEntry.TABLE_NAME, QueryEntry._ID, selectionArgs);
    }

    public int update(QueryHistory queryHistory){
        ContentValues values = new ContentValues();
        values.put(QueryEntry.COLUMN_NAME_DESCRIPTION, queryHistory.getDescription());
        values.put(QueryEntry.COLUMN_NAME_LINK, queryHistory.getLink());
        values.put(QueryEntry.COLUMN_NAME_DATE_UPDATE, queryHistory.getDateUpdate());
        values.put(QueryEntry.COLUMN_NAME_VISUALIZED, queryHistory.getVisualized());
        values.put(QueryEntry.COLUMN_NAME_IS_FAVORITE, queryHistory.getIsFavorite());
        values.put(QueryEntry.COLUMN_NAME_CURRENT_POSITION, queryHistory.getCurrentPosition());
        return db.update(QueryEntry.TABLE_NAME, values, QueryEntry._ID + " = " + queryHistory.getId().toString(), null);
    }

}
