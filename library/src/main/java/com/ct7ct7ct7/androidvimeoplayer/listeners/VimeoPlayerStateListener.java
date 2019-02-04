package com.ct7ct7ct7.androidvimeoplayer.listeners;

public interface VimeoPlayerStateListener {
    void onPlaying(float duration);

    void onPaused(float seconds);

    void onEnded(float duration);
}
