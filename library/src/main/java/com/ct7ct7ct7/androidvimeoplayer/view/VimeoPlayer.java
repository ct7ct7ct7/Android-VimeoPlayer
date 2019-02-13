package com.ct7ct7ct7.androidvimeoplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.utils.JsBridge;
import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;
import com.ct7ct7ct7.androidvimeoplayer.model.VimeoOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VimeoPlayer extends WebView {
    private Handler mainThreadHandler;
    private int videoId;
    private String hashKey;
    private String baseUrl;
    private JsBridge jsBridge;
    private VimeoOptions vimeoOptions;
    private VimeoPlayerReadyListener resetReadyListener;

    public VimeoPlayer(Context context) {
        this(context, null);
    }

    public VimeoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VimeoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void destroy() {
        mainThreadHandler.removeCallbacksAndMessages(null);
        super.destroy();
    }

    public void loadVideo(final int videoId) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:loadVideo('" + videoId + "')");
            }
        });
    }

    public void playTwoStage() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:playTwoStage()");
            }
        });
    }

    public void play() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:playVideo()");
            }
        });
    }

    public void pause() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:pauseVideo()");
            }
        });
    }


    public void seekTo(final float time) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:seekTo(" + time + ")");
            }
        });
    }

    public void setVolume(final float volume) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:setVolume(" + volume + ")");
            }
        });
    }


    public void setTopicColor(final String hexColor) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:setColor('" + hexColor + "')");
            }
        });
    }


    public void setLoop(final boolean loop) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:setLoop(" + loop + ")");
            }
        });
    }


    public void setPlaybackRate(final float playbackRate) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:setPlaybackRate(" + playbackRate + ")");
            }
        });
    }


    protected void destroyPlayer() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                loadUrl("javascript:destroyPlayer()");
            }
        });
    }


    private void initWebView(JsBridge jsBridge, VimeoOptions vimeoOptions, int videoId, String hashKey, String baseUrl) {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setMediaPlaybackRequiresUserGesture(false);
        this.addJavascriptInterface(jsBridge, "JsBridge");

        final String unformattedString = readVimeoPlayerHTMLFromFile();
        String videoUrl = "https://vimeo.com/" + videoId;
        if (hashKey != null) {
            videoUrl += "/" + hashKey;
        }

        boolean autoPlay = false;
        if (!vimeoOptions.originalControls) {
            autoPlay = false;
        } else {
            autoPlay = vimeoOptions.autoPlay;
        }

        final String formattedString = unformattedString
                .replace("<VIDEO_URL>", videoUrl)
                .replace("<AUTOPLAY>", String.valueOf(autoPlay))
                .replace("<LOOP>", String.valueOf(vimeoOptions.loop))
                .replace("<MUTED>", String.valueOf(vimeoOptions.muted))
                .replace("<PLAYSINLINE>", String.valueOf(vimeoOptions.originalControls))
                .replace("<TITLE>", String.valueOf(vimeoOptions.title))
                .replace("<COLOR>", Utils.colorToHex(vimeoOptions.color));


        this.loadDataWithBaseURL(baseUrl, formattedString, "text/html", "utf-8", null);

        // if the video's thumbnail is not in memory, show a black screen
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                Bitmap result = super.getDefaultVideoPoster();

                if (result == null)
                    return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
                else
                    return result;
            }
        });
    }

    private String readVimeoPlayerHTMLFromFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.vimeo_player);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String read;
            StringBuilder sb = new StringBuilder();

            while ((read = bufferedReader.readLine()) != null)
                sb.append(read).append("\n");
            inputStream.close();

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Can't parse HTML file containing the player.");
        }
    }

    public void reset(final PlayerState playerState, final float playAt) {
        switch (playerState) {
            case PLAYING: {
                vimeoOptions.autoPlay = true;
            }
            default: {
                vimeoOptions.autoPlay = false;
            }
        }

        if (resetReadyListener != null) {
            jsBridge.removeLastReadyListener(resetReadyListener);
        }
        resetReadyListener = new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String title, float duration) {
                seekTo(playAt);
            }

            @Override
            public void onInitFailed() {

            }
        };
        jsBridge.addReadyListener(resetReadyListener);

        initWebView(jsBridge, vimeoOptions, videoId, hashKey, baseUrl);
    }

    protected void initialize(JsBridge jsBridge, VimeoOptions vimeoOptions, int videoId, String hashKey, String baseUrl) {
        this.jsBridge = jsBridge;
        this.vimeoOptions = vimeoOptions;
        this.videoId = videoId;
        this.hashKey = hashKey;
        this.baseUrl = baseUrl;
        initWebView(jsBridge, vimeoOptions, videoId, hashKey, baseUrl);
    }

    public void recycle(){
        destroyPlayer();
        this.setTag(null);
        this.clearHistory();
        this.destroy();
    }
}
