package com.alexandrealencar.videoclean.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;

public class DownloadActivity extends VideoCleanActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        recyclerView = findViewById(R.id.recyclerViewLinkVideo);
        linkAdapter = new LinkPageAdapter(this);
        linkAdapter.setmDataset(videoCleanController.selectDownloads());
        recyclerView.setAdapter(linkAdapter);
    }
}
