package com.alexandrealencar.videoclean.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import com.alexandrealencar.videoclean.R;

import java.util.List;


public class VideoActivity extends AppCompatActivity {
    VideoView videoView = null;
    MediaController mediaController = null;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        videoView.setVideoURI( uri );
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView( videoView );
        videoView.start();



        /*final ProgressBar spinnerView = findViewById(R.id.my_spinner);
        final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    spinnerView.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    spinnerView.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    spinnerView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
        videoView.setOnInfoListener(onInfoToPlayStateListener);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null){
            position = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.seekTo(position);
            videoView.start();
        }
    }
}
