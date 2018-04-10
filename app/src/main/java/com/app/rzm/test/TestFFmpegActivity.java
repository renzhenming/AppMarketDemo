package com.app.rzm.test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;

import com.app.rzm.R;
import com.app.rzm.utils.FFmpegUtils;
import com.app.rzm.utils.VideoView;

import java.io.File;

public class TestFFmpegActivity extends AppCompatActivity {

    private VideoView videoView;
    private FFmpegUtils player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ffmpeg);
        videoView = (VideoView) findViewById(R.id.video_view);
        player = new FFmpegUtils();
    }

    public void play(View view) {
        String input = new File(Environment.getExternalStorageDirectory(),"input.mp4").getAbsolutePath();
        Surface surface = videoView.getHolder().getSurface();
        player.play(input, surface);
    }
}
