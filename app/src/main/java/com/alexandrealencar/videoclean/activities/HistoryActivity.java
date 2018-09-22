package com.alexandrealencar.videoclean.activities;

import android.database.Cursor;
import android.os.Bundle;
import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.QueryContract.QueryEntry;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends VideoCleanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recyclerViewLinkPage);
        linkAdapter = new LinkPageAdapter(this);
        linkAdapter.setmDataset( getListLinks( videoCleanController.selectHistory() ) );
        recyclerView.setAdapter(linkAdapter);
    }

    private List<String[]> getListLinks(Cursor cursor){
        List<String[]> links = new ArrayList<>();
        while(cursor.moveToNext()){
            String description = cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_DESCRIPTION));
            String link = cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_LINK));
            links.add(new String[]{link,description});
        }
        cursor.close();
        return links;
    }

    @Override
    protected void onResume() {
        super.onResume();
        linkAdapter.setmDataset( getListLinks( videoCleanController.selectHistory() ) );
        recyclerView.setAdapter(linkAdapter);
    }
}