package com.ct7ct7ct7.androidvimeoplayer.listeners;

import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;

public interface VimeoPlayerReadyListener {
    void onReady(String title, float duration, TextTrack[] textTrackArray);

    void onInitFailed();
}
