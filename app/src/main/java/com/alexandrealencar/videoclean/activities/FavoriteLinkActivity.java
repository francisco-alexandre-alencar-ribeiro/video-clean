package com.alexandrealencar.videoclean.activities;

import android.database.Cursor;
import android.os.Bundle;

import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.QueryContract.QueryEntry;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLinkActivity extends VideoCleanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_link);
        recyclerView = findViewById(R.id.recyclerViewLinkVideo);
        linkAdapter = new LinkPageAdapter(this);
        linkAdapter.setmDataset(videoCleanController.selectFavorite());
        recyclerView.setAdapter(linkAdapter);
    }
}