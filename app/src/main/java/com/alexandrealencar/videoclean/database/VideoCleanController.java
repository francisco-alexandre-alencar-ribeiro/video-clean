package com.alexandrealencar.videoclean.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alexandrealencar.videoclean.database.QueryContract.QueryEntry;
import com.alexandrealencar.videoclean.entities.QueryHistory;

import java.util.ArrayList;
import java.util.List;

public final class VideoCleanController {
    private SQLiteDatabase db;
    private Context context;
    private String[] columns = {QueryEntry._ID, QueryEntry.COLUMN_NAME_LINK, QueryEntry.COLUMN_NAME_DESCRIPTION , QueryEntry.COLUMN_NAME_IS_FAVORITE , QueryEntry.COLUMN_NAME_CURRENT_POSITION, QueryEntry.COLUMN_NAME_IS_DOWNLOAD , QueryEntry.COLUMN_NAME_PATH, QueryEntry.COLUMN_NAME_VISUALIZED};


    public VideoCleanController(Context context) {
        this.context = context;
    }

    private void getConnection(){
        db = new VideoCleanDB(context).getWritableDatabase();
    }

    public Cursor select( String selection , String[] selectionArqs ) {
        getConnection();
        return db.query(QueryEntry.TABLE_NAME, columns, selection, selectionArqs, null, null, QueryEntry._ID );
    }

    public List<String[]> selectFavorite() {
        getConnection();
        Cursor cursor = db.query(QueryEntry.TABLE_NAME, columns, QueryEntry.COLUMN_NAME_IS_FAVORITE + " = ? ", new String[]{ "1" } , null, null, QueryEntry._ID );
        return cursorToList(cursor);
    }

    public List<String[]> selectHistory() {
        getConnection();
        Cursor cursor = db.query(QueryEntry.TABLE_NAME, columns, QueryEntry.COLUMN_NAME_VISUALIZED + " = ? ", new String[]{ "1" } , null, null, QueryEntry.COLUMN_NAME_DATE_UPDATE + " DESC" );
        return cursorToList(cursor);
    }


    public List<String[]> selectDownloads() {
        getConnection();
        Cursor cursor = db.query(QueryEntry.TABLE_NAME, columns, QueryEntry.COLUMN_NAME_IS_DOWNLOAD + " = ? ", new String[]{ "1" } , null, null, QueryEntry.COLUMN_NAME_DATE_UPDATE + " DESC" );
        return cursorToList(cursor);
    }

    public long insert(QueryHistory queryHistory) {
        getConnection();
        long id = db.insert(QueryEntry.TABLE_NAME, null, getValues(queryHistory) );
        db.close();
        return id;
    }

    public long delete(QueryHistory queryHistory) {
        getConnection();
        String[] selectionArgs = { queryHistory.getId().toString() };
        long id = db.delete(QueryEntry.TABLE_NAME, QueryEntry._ID, selectionArgs);
        db.close();
        return id;
    }

    public long update(QueryHistory queryHistory){
        getConnection();
        long id = db.update(QueryEntry.TABLE_NAME, getValues(queryHistory), QueryEntry._ID + " = " + queryHistory.getId().toString(), null);
        db.close();
        return id;
    }

    private List<String[]> cursorToList(Cursor cursor){
        List<String[]> links = new ArrayList<>();
        while(cursor.moveToNext()){
            String link = "";
            String description = cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_DESCRIPTION));
            if ( cursor.getInt(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_IS_DOWNLOAD)) == 1 ){
                link = cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_PATH));
            } else {
                link = cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_LINK));
            }
            links.add(new String[]{link,description});
        }
        cursor.close();
        db.close();
        return links;
    }


    private ContentValues getValues( QueryHistory queryHistory ){
        ContentValues values = new ContentValues();
        values.put(QueryEntry.COLUMN_NAME_DESCRIPTION, queryHistory.getDescription());
        values.put(QueryEntry.COLUMN_NAME_LINK, queryHistory.getLink());
        values.put(QueryEntry.COLUMN_NAME_DATE_UPDATE, queryHistory.getDateUpdate());
        values.put(QueryEntry.COLUMN_NAME_VISUALIZED, queryHistory.getVisualized());
        values.put(QueryEntry.COLUMN_NAME_IS_FAVORITE, queryHistory.getIsFavorite());
        values.put(QueryEntry.COLUMN_NAME_CURRENT_POSITION, queryHistory.getCurrentPosition());
        values.put(QueryEntry.COLUMN_NAME_PATH, queryHistory.getPath());
        values.put(QueryEntry.COLUMN_NAME_IS_DOWNLOAD, queryHistory.getIsDownload());
        return values;
    }
}
