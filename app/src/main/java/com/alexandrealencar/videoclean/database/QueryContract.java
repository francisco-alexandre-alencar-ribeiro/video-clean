package com.alexandrealencar.videoclean.database;

import android.provider.BaseColumns;

public final class QueryContract {

    public static class QueryEntry implements BaseColumns {
        public static final String TABLE_NAME = "query_history";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DATE_UPDATE = "date_update";
        public static final String COLUMN_NAME_DATE_CREATE = "date_create";
        public static final String COLUMN_NAME_VISUALIZED = "visualized";
        public static final String COLUMN_NAME_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_NAME_CURRENT_POSITION = "current_position";
    }
}
