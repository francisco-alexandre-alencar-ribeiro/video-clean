package com.alexandrealencar.videoclean.activities;

import android.os.Bundle;
import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;

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

    @Override
    public void afterDownload(){
        linkAdapter.setmDataset(videoCleanController.selectFavorite());
        recyclerView.setAdapter(linkAdapter);
    }
}