package com.alexandrealencar.videoclean.database;

import android.provider.BaseColumns;

public final class QueryHistoryContract {
    
    public static class QueryHistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "query_history";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DATE_UPDATE = "date_update";
    }
}
