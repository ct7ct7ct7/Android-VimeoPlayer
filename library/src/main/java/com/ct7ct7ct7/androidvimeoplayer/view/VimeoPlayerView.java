package com.ct7ct7ct7.androidvimeoplayer.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerErrorListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.utils.JsBridge;
import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;
import com.ct7ct7ct7.androidvimeoplayer.model.VimeoOptions;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTextTrackListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerVolumeListener;

public class VimeoPlayerView extends FrameLayout implements LifecycleObserver {
    public VimeoOptions defaultOptions;
    public int defaultColor = Color.rgb(0, 172, 240);
    private JsBridge jsBridge;
    private VimeoPlayer vimeoPlayer;
    private ProgressBar progressBar;
    private DefaultControlPanelView defaultControlPanelView;
    private String title;

    public VimeoPlayerView(Context context) {
        this(context, null);
    }

    public VimeoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VimeoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.jsBridge = new JsBridge();
        jsBridge.addReadyListener(new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String t, float duration) {
                title = t;
                progressBar.setVisibility(View.GONE);
                if (!defaultOptions.originalControls) {
                    if (defaultOptions.autoPlay) {
                        vimeoPlayer.playTwoStage();
                        defaultControlPanelView.dismissControls(4000);
                    }
                }
            }

            @Override
            public void onInitFailed() {
                progressBar.setVisibility(View.GONE);
            }
        });

        defaultOptions = generateDefaultVimeoOptions(context, attrs);
        this.vimeoPlayer = new VimeoPlayer(context);
        this.addView(vimeoPlayer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        if (!defaultOptions.originalControls) {
            defaultControlPanelView = new DefaultControlPanelView(this);
        }

        this.progressBar = new ProgressBar(context);
        if (defaultOptions.color != defaultColor) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.setIndeterminate(true);
                progressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(defaultOptions.color));
            }
        }
        LayoutParams progressLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        progressLayoutParams.gravity = Gravity.CENTER;
        this.addView(progressBar, progressLayoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //16:9
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            int sixteenNineHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, sixteenNineHeight);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addReadyListener(VimeoPlayerReadyListener readyListener) {
        jsBridge.addReadyListener(readyListener);
    }

    public void addStateListener(VimeoPlayerStateListener stateListener) {
        jsBridge.addStateListener(stateListener);
    }

    public void addTextTrackListener(VimeoPlayerTextTrackListener textTrackListener) {
        jsBridge.addTextTrackListener(textTrackListener);
    }

    public void addTimeListener(VimeoPlayerTimeListener timeListener) {
        jsBridge.addTimeListener(timeListener);
    }

    public void addVolumeListener(VimeoPlayerVolumeListener volumeListener) {
        jsBridge.addVolumeListener(volumeListener);
    }

    public void addErrorListener(VimeoPlayerErrorListener errorListener) {
        jsBridge.addErrorListener(errorListener);
    }

    public void loadVideo(int videoId) {
        vimeoPlayer.loadVideo(videoId);
    }

    public void play() {
        vimeoPlayer.play();
    }

    public void pause() {
        vimeoPlayer.pause();
    }

    public void seekTo(float time) {
        vimeoPlayer.seekTo(time);
    }

    /**
     * @param volume 0.0 to 1.0
     */
    public void setVolume(float volume) {
        if (volume > 1) volume = 1;
        vimeoPlayer.setVolume(volume);
    }

    public String getVideoTitle() {
        return title;
    }

    public void setTopicColor(int color) {
        vimeoPlayer.setTopicColor(Utils.colorToHex(color));
    }

    public void setLoop(boolean loop) {
        vimeoPlayer.setLoop(loop);
    }

    public void setFullscreenClickListener(final OnClickListener onClickListener) {
        if (defaultControlPanelView != null) {
            defaultControlPanelView.setFullscreenClickListener(onClickListener);
        }
    }

    public void showFullscreenButton(boolean show) {
        if (defaultControlPanelView != null) {
            defaultControlPanelView.setFullscreenVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setSettingsClickListener(final OnClickListener onClickListener) {
        if (defaultControlPanelView != null) {
            defaultControlPanelView.setSettingsClickListener(onClickListener);
        }
    }

    public void showSettingsButton(boolean show) {
        if (defaultControlPanelView != null) {
            defaultControlPanelView.setSettingsVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @param playbackRate 0.5 to 2
     */
    public void setPlaybackRate(float playbackRate) {
        if (playbackRate > 2) playbackRate = 2;
        if (playbackRate < 0.5) playbackRate = 0.5f;

        vimeoPlayer.setPlaybackRate(playbackRate);
    }

    public float getCurrentTimeSeconds() {
        return jsBridge.currentTimeSeconds;
    }

    public PlayerState getPlayerState() {
        return jsBridge.playerState;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        vimeoPlayer.destroyPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        vimeoPlayer.pause();
    }

    private VimeoOptions generateDefaultVimeoOptions(Context context, AttributeSet attrs) {
        VimeoOptions options = new VimeoOptions();

        if (!isInEditMode()) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VimeoPlayerView);

            boolean autoPlay = attributes.getBoolean(R.styleable.VimeoPlayerView_autoPlay, false);
            boolean loop = attributes.getBoolean(R.styleable.VimeoPlayerView_loop, false);
            boolean muted = attributes.getBoolean(R.styleable.VimeoPlayerView_muted, false);
            boolean originalControls = attributes.getBoolean(R.styleable.VimeoPlayerView_showOriginalControls, false);
            boolean title = attributes.getBoolean(R.styleable.VimeoPlayerView_showTitle, true);
            int color = attributes.getColor(R.styleable.VimeoPlayerView_topicColor, defaultColor);

            options.autoPlay = autoPlay;
            options.loop = loop;
            options.muted = muted;
            options.originalControls = originalControls;
            options.title = title;
            options.color = color;
        }

        return options;
    }

    /**
     * @param videoId the video id.
     * @param hashKey if your video is private, you MUST pass the private video hash key.
     * @param baseUrl settings embedded url. e.g. https://yourdomain
     */
    public void initialize(int videoId, String hashKey, String baseUrl) {
        vimeoPlayer.initialize(jsBridge, defaultOptions, videoId, hashKey, baseUrl);
    }

    /**
     * @param videoId the video id.
     * @param baseUrl settings embedded url. e.g. https://yourdomain
     */
    public void initialize(int videoId, String baseUrl) {
        vimeoPlayer.initialize(jsBridge, defaultOptions, videoId, null, baseUrl);
    }

    /**
     * @param videoId the video id.
     */
    public void initialize(int videoId) {
        this.initialize(videoId, null, null);
    }
}
