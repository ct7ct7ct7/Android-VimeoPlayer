package com.ct7ct7ct7.androidvimeoplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;
import com.ct7ct7ct7.androidvimeoplayer.utils.JsBridge;
import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;
import com.ct7ct7ct7.androidvimeoplayer.model.VimeoOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VimeoPlayer extends WebView {
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
    }


    public void loadVideo(final int videoId) {
        String script = "javascript:loadVideo('" + videoId + "')";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    public void playTwoStage() {
        evaluateJavascript("javascript:playTwoStage()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    public void play() {
        evaluateJavascript("javascript:playVideo()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    public void pause() {
        evaluateJavascript("javascript:pauseVideo()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    public void seekTo(final float time) {
        String script = "javascript:seekTo(" + time + ")";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams parentLp = ((ViewGroup) getParent()).getLayoutParams();
        if (parentLp.width == ViewGroup.LayoutParams.MATCH_PARENT && parentLp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (widthMeasureSpec < heightMeasureSpec) {
                int heightByRatio = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * vimeoOptions.aspectRatio), MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightByRatio);
            } else {
                int widthByRatio = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(heightMeasureSpec) * vimeoOptions.aspectRatio), MeasureSpec.EXACTLY);
                super.onMeasure(widthByRatio, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setVolume(final float volume) {
        String script = "javascript:setVolume(" + volume + ")";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    public void setTopicColor(final String hexColor) {
        String script = "javascript:setColor('" + hexColor + "')";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    public void setLoop(final boolean loop) {
        String script = "javascript:setLoop(" + loop + ")";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    public void setPlaybackRate(final float playbackRate) {
        String script = "javascript:setPlaybackRate(" + playbackRate + ")";
        evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    public void setCaptions(final String language) {
        evaluateJavascript("javascript:setCaptions('" + language + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    public void disableCaptions() {
        evaluateJavascript("javascript:disableCaptions()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    protected void destroyPlayer() {
        evaluateJavascript("javascript:destroyPlayer()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(JsBridge jsBridge, VimeoOptions vimeoOptions, int videoId, String hashKey, String baseUrl) {
        clearWebViewCache();

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setSupportMultipleWindows(false);
        this.getSettings().setAppCacheEnabled(false);
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.getSettings().setMediaPlaybackRequiresUserGesture(false);

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
                .replace("<COLOR>", Utils.colorToHex(vimeoOptions.color))
                .replace("<BACKGROUND_COLOR>", Utils.colorToHex(vimeoOptions.backgroundColor))
                .replace("<QUALITY>", vimeoOptions.quality);


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

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String url) {
                webView.evaluateJavascript("javascript:initVimeoPlayer()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        };
        setWebViewClient(webViewClient);
    }


    private void clearWebViewCache() {
        clearCache(true);
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
            public void onReady(String title, float duration, TextTrack[] textTrackArray) {
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

    public void recycle() {
        clearWebViewCache();
        destroyPlayer();
        this.setTag(null);
    }
}
