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
        linkAdapter.setmDataset(videoCleanController.selectHistory());
        recyclerView.setAdapter(linkAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        linkAdapter.setmDataset(videoCleanController.selectHistory());
        recyclerView.setAdapter(linkAdapter);
    }

    @Override
    public void afterDownload(){
        linkAdapter.setmDataset(videoCleanController.selectHistory());
        recyclerView.setAdapter(linkAdapter);
    }
}