package com.alexandrealencar.videoclean.activities;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.database.VideoCleanController;
import com.alexandrealencar.videoclean.entities.QueryHistory;
import com.alexandrealencar.videoclean.database.QueryContract.QueryEntry;

import java.io.IOException;
import java.util.Date;

public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnInfoListener {
    VideoView videoView = null;
    MediaController mediaController = null;
    VideoCleanController videoCleanController = null;
    QueryHistory queryHistory = null;
    ProgressBar spinnerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);

        String url = getIntent().getStringArrayExtra(QueryEntry.COLUMN_NAME_LINK)[0];
        videoView.setVideoURI(Uri.parse(url));
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoCleanController = new VideoCleanController(this);
        Cursor cursor = videoCleanController.select( QueryEntry.COLUMN_NAME_LINK + " = ? OR " + QueryEntry.COLUMN_NAME_PATH + " = ? ", new String[]{ url , url });
        queryHistory = new QueryHistory();
        queryHistory.setVisualized(1);
        queryHistory.setLink(url);
        queryHistory.setDateUpdate(new Date().getTime());
        if (!cursor.moveToNext()) {
            queryHistory.setDescription(url);
            queryHistory.setDateCreate(new Date().getTime());
            queryHistory.setId(videoCleanController.insert(queryHistory));
        } else {
            queryHistory.setDescription(cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_DESCRIPTION)));
            queryHistory.setIsDownload(cursor.getInt(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_IS_DOWNLOAD)));
            queryHistory.setPath(cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_PATH)));
            queryHistory.setLink(cursor.getString(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_LINK)));
            queryHistory.setId(cursor.getLong(cursor.getColumnIndex(QueryEntry._ID)));
            queryHistory.setIsFavorite(cursor.getInt(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_IS_FAVORITE)));
            queryHistory.setCurrentPosition(cursor.getInt(cursor.getColumnIndex(QueryEntry.COLUMN_NAME_CURRENT_POSITION)));
            videoCleanController.update(queryHistory);
        }
        videoView.seekTo(queryHistory.getCurrentPosition());
        videoView.start();
        videoView.setOnInfoListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            queryHistory.setCurrentPosition((videoView.getCurrentPosition() == 0) ? queryHistory.getCurrentPosition() : videoView.getCurrentPosition());
            videoCleanController.update(queryHistory);
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.seekTo(queryHistory.getCurrentPosition());
            videoView.start();
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        spinnerView = findViewById(R.id.spinner);
        if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
            spinnerView.setVisibility(View.GONE);
        }
        if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
            spinnerView.setVisibility(View.VISIBLE);
        }
        if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
            spinnerView.setVisibility(View.INVISIBLE);
        }
        return false;
    }
}
