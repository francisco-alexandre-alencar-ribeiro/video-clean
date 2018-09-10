package com.alexandrealencar.videoclean.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.VideoCleanController;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLinkActivity extends AppCompatActivity  implements LinkPageAdapter.OnListInteraction {
    private RecyclerView recyclerView = null;
    private LinkPageAdapter linkAdapter = null;
    private VideoCleanController videoCleanController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_link);
        recyclerView = findViewById(R.id.recyclerViewLinkVideo);
        linkAdapter = new LinkPageAdapter(this);
        videoCleanController = new VideoCleanController(this);
        linkAdapter.setmDataset(getListLinks(videoCleanController.select()));
        recyclerView.setAdapter(linkAdapter);
    }

    private List<String> getListLinks(Cursor cursor){
        List<String> links = new ArrayList<>();
        while(cursor.moveToNext()){
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String link = cursor.getString(cursor.getColumnIndex("link"));
            links.add(link + ',' + description);
        }
        cursor.close();
        return links;
    }

    @Override
    public void onClickIem(String[] url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, url[0]);
        intent.setAction(Intent.ACTION_SEND);
        startActivity(intent);
    }
}
