package com.ct7ct7ct7.androidvimeoplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;

public class VimeoPlayerActivity extends AppCompatActivity {
    public static final String RESULT_STATE_VIDEO_ID = "RESULT_STATE_VIDEO_ID";
    public static final String RESULT_STATE_VIDEO_PLAY_AT = "RESULT_STATE_VIDEO_PLAY_AT";
    public static final String RESULT_STATE_PLAYER_STATE = "RESULT_STATE_PLAYER_STATE";

    private static final String EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID";
    private static final String EXTRA_HASH_KEY = "EXTRA_HASH_KEY";
    private static final String EXTRA_BASE_URL = "EXTRA_BASE_URL";
    private static final String EXTRA_START_AT = "EXTRA_START_AT";
    private static final String EXTRA_END_AT = "EXTRA_END_AT";
    private static final String EXTRA_TOPIC_COLOR = "EXTRA_TOPIC_COLOR";
    private static final String EXTRA_LOOP = "EXTRA_LOOP";
    private static final String EXTRA_SETTINGS_OPTION_VISIBLE = "EXTRA_SETTINGS_OPTION_VISIBLE";

    private VimeoPlayerView vimeoPlayerView;
    private int videoId;
    private String hashKey;
    private String baseUrl;
    private float startAt;
    private float endAt;
    private int topicColor;
    private boolean loop;
    private boolean settingsOptionVisible;

    public static Intent createIntent(Context context, VimeoPlayerView vimeoPlayerView) {
        Intent intent = new Intent(context, VimeoPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, vimeoPlayerView.getVideoId());
        intent.putExtra(EXTRA_HASH_KEY, vimeoPlayerView.getHashKey());
        intent.putExtra(EXTRA_BASE_URL, vimeoPlayerView.getBaseUrl());
        intent.putExtra(EXTRA_START_AT, vimeoPlayerView.getCurrentTimeSeconds());
        intent.putExtra(EXTRA_TOPIC_COLOR, vimeoPlayerView.getTopicColor());
        intent.putExtra(EXTRA_LOOP, vimeoPlayerView.getLoop());
        intent.putExtra(EXTRA_SETTINGS_OPTION_VISIBLE, vimeoPlayerView.getSettingsVisibility());
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_player);
        vimeoPlayerView = findViewById(R.id.vimeoPlayerView);
        videoId = getIntent().getIntExtra(EXTRA_VIDEO_ID, 0);
        hashKey = getIntent().getStringExtra(EXTRA_HASH_KEY);
        baseUrl = getIntent().getStringExtra(EXTRA_BASE_URL);
        startAt = getIntent().getFloatExtra(EXTRA_START_AT, 0f);
        endAt = getIntent().getFloatExtra(EXTRA_END_AT, Float.MAX_VALUE);
        topicColor = getIntent().getIntExtra(EXTRA_TOPIC_COLOR, Color.rgb(0, 172, 240));
        loop = getIntent().getBooleanExtra(EXTRA_LOOP,false);
        settingsOptionVisible = getIntent().getBooleanExtra(EXTRA_SETTINGS_OPTION_VISIBLE,false);

        vimeoPlayerView.showMenuOption(settingsOptionVisible);
        vimeoPlayerView.setLoop(loop);
        vimeoPlayerView.setTopicColor(topicColor);
        vimeoPlayerView.initialize(videoId, hashKey, baseUrl);
        vimeoPlayerView.addReadyListener(new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String title, float duration) {
                vimeoPlayerView.seekTo(startAt);
                vimeoPlayerView.playTwoStage();
            }

            @Override
            public void onInitFailed() {

            }
        });
        vimeoPlayerView.addTimeListener(new VimeoPlayerTimeListener() {
            @Override
            public void onCurrentSecond(float second) {
                if (second >= endAt) {
                    vimeoPlayerView.pause();
                }
            }
        });
        vimeoPlayerView.setFullscreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_STATE_VIDEO_ID, videoId);
        intent.putExtra(RESULT_STATE_VIDEO_PLAY_AT, vimeoPlayerView.getCurrentTimeSeconds());
        intent.putExtra(RESULT_STATE_PLAYER_STATE, vimeoPlayerView.getPlayerState().name());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
