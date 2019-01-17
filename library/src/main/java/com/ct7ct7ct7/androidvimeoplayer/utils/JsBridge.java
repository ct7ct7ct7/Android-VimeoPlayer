package com.ct7ct7ct7.androidvimeoplayer.utils;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerErrorListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTextTrackListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerVolumeListener;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;


public class JsBridge {
    private final Handler mainThreadHandler;
    private VimeoPlayerReadyListener readyListener;
    private VimeoPlayerStateListener stateListener;
    private VimeoPlayerTextTrackListener textTrackListener;
    private VimeoPlayerTimeListener timeListener;
    private VimeoPlayerVolumeListener volumeListener;
    private VimeoPlayerErrorListener errorListener;

    public float currentTimeSeconds = 0;
    public PlayerState playerState = PlayerState.UNKNOWN;
    public float volume = 1;

    public JsBridge(VimeoPlayerReadyListener readyListener) {
        this.readyListener = readyListener;
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void addStateListener(VimeoPlayerStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public void addTextTrackListener(VimeoPlayerTextTrackListener textTrackListener) {
        this.textTrackListener = textTrackListener;
    }

    public void addTimeListener(VimeoPlayerTimeListener timeListener) {
        this.timeListener = timeListener;
    }

    public void addVolumeListener(VimeoPlayerVolumeListener volumeListener) {
        this.volumeListener = volumeListener;
    }

    public void addErrorListener(VimeoPlayerErrorListener errorListener) {
        this.errorListener = errorListener;
    }


    @JavascriptInterface
    public void sendVideoCurrentTime(float seconds) {
        currentTimeSeconds = seconds;
        if (timeListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    timeListener.onCurrentSecond(currentTimeSeconds);
                }
            });
        }
    }

    @JavascriptInterface
    public void sendError(final String message, final String method, final String name) {
        if (errorListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    errorListener.onError(message, method, name);
                }
            });
        }
    }


    @JavascriptInterface
    public void sendReady() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                readyListener.onReady();
            }
        });
    }

    @JavascriptInterface
    public void sendInitFailed() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                readyListener.onInitFailed();
            }
        });

        if (stateListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    stateListener.onInitFailed();
                }
            });
        }
    }


    @JavascriptInterface
    public void sendPlaying(final float duration) {
        playerState = PlayerState.PLAYING;
        if (stateListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    stateListener.onPlaying(duration);
                }
            });
        }
    }


    @JavascriptInterface
    public void sendPaused(final float seconds) {
        playerState = PlayerState.PAUSED;
        if (stateListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    stateListener.onPaused(seconds);
                }
            });
        }
    }

    @JavascriptInterface
    public void sendEnded(final float duration) {
        playerState = PlayerState.ENDED;
        if (stateListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    stateListener.onEnded(duration);
                }
            });
        }
    }

    @JavascriptInterface
    public void sendLoaded(final int videoId) {
        this.playerState = PlayerState.LOADED;
        if (stateListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    stateListener.onLoaded(videoId);
                }
            });
        }
    }


    @JavascriptInterface
    public void sendVolumeChange(final float volume) {
        this.volume = volume;
        if (volumeListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    volumeListener.onVolumeChanged(volume);
                }
            });
        }
    }

    @JavascriptInterface
    public void sendTextTrackChange(final String kind, final String label, final String language) {
        if (textTrackListener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    textTrackListener.onTextTrackChanged(kind, label, language);
                }
            });
        }
    }
}
