package com.ct7ct7ct7.androidvimeoplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;
import com.ct7ct7ct7.androidvimeoplayer.model.VimeoThumbnail;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;
import com.ct7ct7ct7.androidvimeoplayer.view.menu.ViemoMenuItem;
import com.ct7ct7ct7.androidvimeoplayer.view.menu.ViemoPlayerMenu;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultControlPanelView {
    private View vimeoPanelView;
    private View vimeoShadeView;
    private ImageView vimeoMenuButton;
    private ImageView vimeoFullscreenButton;
    private SeekBar vimeoSeekBar;
    private TextView vimeoCurrentTimeTextView;
    private ImageView vimeoThumbnailImageView;
    private ImageView vimeoPlayButton;
    private ImageView vimeoPauseButton;
    private ImageView vimeoReplayButton;
    private TextView vimeoTitleTextView;
    private View controlsRootView;
    private boolean ended = false;
    private ViemoPlayerMenu vimeoPlayerMenu;

    public DefaultControlPanelView(final VimeoPlayerView vimeoPlayerView) {
        View defaultControlPanelView = View.inflate(vimeoPlayerView.getContext(), R.layout.view_default_control_panel, vimeoPlayerView);
        vimeoPanelView = defaultControlPanelView.findViewById(R.id.vimeoPanelView);
        vimeoShadeView = defaultControlPanelView.findViewById(R.id.vimeoShadeView);
        vimeoMenuButton = defaultControlPanelView.findViewById(R.id.vimeoMenuButton);
        vimeoFullscreenButton = defaultControlPanelView.findViewById(R.id.vimeoFullscreenButton);
        vimeoSeekBar = defaultControlPanelView.findViewById(R.id.vimeoSeekBar);
        vimeoCurrentTimeTextView = defaultControlPanelView.findViewById(R.id.vimeoCurrentTimeTextView);
        vimeoThumbnailImageView = defaultControlPanelView.findViewById(R.id.vimeoThumbnailImageView);
        vimeoPlayButton = defaultControlPanelView.findViewById(R.id.vimeoPlayButton);
        vimeoPauseButton = defaultControlPanelView.findViewById(R.id.vimeoPauseButton);
        vimeoReplayButton = defaultControlPanelView.findViewById(R.id.vimeoReplayButton);
        vimeoTitleTextView = defaultControlPanelView.findViewById(R.id.vimeoTitleTextView);
        controlsRootView = defaultControlPanelView.findViewById(R.id.controlsRootView);
        vimeoPlayerMenu = new ViemoPlayerMenu(vimeoPlayerView.getContext());

        vimeoSeekBar.setVisibility(View.INVISIBLE);
        vimeoPanelView.setVisibility(View.VISIBLE);
        vimeoShadeView.setVisibility(View.VISIBLE);
        vimeoThumbnailImageView.setVisibility(View.VISIBLE);
        controlsRootView.setVisibility(View.GONE);

        vimeoPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vimeoPlayerView.pause();
                dismissControls(4000);
            }
        });
        vimeoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vimeoPlayerView.play();
            }
        });
        vimeoReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vimeoPlayerView.seekTo(0);
                vimeoPlayerView.play();
            }
        });


        vimeoPlayerView.addTimeListener(new VimeoPlayerTimeListener() {
            @Override
            public void onCurrentSecond(float second) {
                vimeoCurrentTimeTextView.setText(Utils.formatTime(second));
                vimeoSeekBar.setProgress((int) second);
            }
        });

        vimeoPlayerView.addReadyListener(new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String title, float duration, TextTrack[] textTrackArray) {
                vimeoSeekBar.setMax((int) duration);
                vimeoTitleTextView.setText(title);
                vimeoPanelView.setVisibility(View.VISIBLE);
                controlsRootView.setVisibility(View.VISIBLE);
                vimeoShadeView.setVisibility(View.GONE);
            }

            @Override
            public void onInitFailed() {

            }
        });

        vimeoPlayerView.addStateListener(new VimeoPlayerStateListener() {
            @Override
            public void onPlaying(float duration) {
                ended = false;
                vimeoSeekBar.setVisibility(View.VISIBLE);
                vimeoPanelView.setBackgroundColor(Color.TRANSPARENT);
                vimeoPauseButton.setVisibility(View.VISIBLE);
                vimeoPlayButton.setVisibility(View.GONE);
                vimeoReplayButton.setVisibility(View.GONE);
                vimeoThumbnailImageView.setVisibility(View.GONE);
                dismissControls(4000);
            }

            @Override
            public void onPaused(float seconds) {
                if (ended) {
                    vimeoPanelView.setBackgroundColor(Color.BLACK);
                    vimeoReplayButton.setVisibility(View.VISIBLE);
                    vimeoPauseButton.setVisibility(View.GONE);
                    vimeoPlayButton.setVisibility(View.GONE);
                } else {
                    vimeoReplayButton.setVisibility(View.GONE);
                    vimeoPauseButton.setVisibility(View.GONE);
                    vimeoPlayButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnded(float duration) {
                ended = true;
                vimeoThumbnailImageView.setVisibility(View.VISIBLE);
                showControls(false);
            }
        });

        vimeoTitleTextView.setVisibility(vimeoPlayerView.defaultOptions.title ? View.VISIBLE : View.INVISIBLE);

        if (vimeoPlayerView.defaultOptions.color != vimeoPlayerView.defaultColor) {
            vimeoSeekBar.getThumb().setColorFilter(vimeoPlayerView.defaultOptions.color, PorterDuff.Mode.SRC_ATOP);
            vimeoSeekBar.getProgressDrawable().setColorFilter(vimeoPlayerView.defaultOptions.color, PorterDuff.Mode.SRC_ATOP);
        }

        vimeoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vimeoCurrentTimeTextView.setText(Utils.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vimeoPlayerView.pause();
                showControls(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vimeoPlayerView.seekTo(seekBar.getProgress());
                vimeoPlayerView.play();
                dismissControls(4000);
            }
        });

        vimeoPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showControls(true);
            }
        });

        vimeoMenuButton.setVisibility(vimeoPlayerView.defaultOptions.menuOption ? View.VISIBLE : View.GONE);
        vimeoFullscreenButton.setVisibility(vimeoPlayerView.defaultOptions.fullscreenOption ? View.VISIBLE : View.GONE);


        vimeoMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vimeoPlayerMenu.show(vimeoMenuButton);
            }
        });
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            controlsRootView.setVisibility(View.GONE);
        }
    };

    public void dismissControls(final int duration) {
        handler.removeCallbacks(dismissRunnable);

        handler.postDelayed(dismissRunnable, duration);
    }

    public void showControls(final boolean autoMask) {
        handler.removeCallbacks(dismissRunnable);

        controlsRootView.setVisibility(View.VISIBLE);
        if (autoMask) {
            dismissControls(3000);
        }
    }

    public void setFullscreenVisibility(int value) {
        vimeoFullscreenButton.setVisibility(value);
    }

    public void setFullscreenClickListener(final View.OnClickListener onClickListener) {
        vimeoFullscreenButton.setOnClickListener(onClickListener);
    }

    public void setMenuVisibility(int value) {
        vimeoMenuButton.setVisibility(value);
    }

    public void addMenuItem(ViemoMenuItem menuItem) {
        vimeoPlayerMenu.addItem(menuItem);
    }

    public void removeMenuItem(int itemIndex) {
        vimeoPlayerMenu.removeItem(itemIndex);
    }

    public int getMenuItemCount() {
        return vimeoPlayerMenu.getItemCount();
    }

    public void dismissMenuItem() {
        vimeoPlayerMenu.dismiss();
    }

    public void setMenuClickListener(final View.OnClickListener onClickListener) {
        vimeoMenuButton.setOnClickListener(onClickListener);
    }

    public void setTopicColor(int color) {
        vimeoSeekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        vimeoSeekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @SuppressLint("StaticFieldLeak")
    protected void fetchThumbnail(final Context context, final int videoId) {
        new AsyncTask<Void, Void, VimeoThumbnail>() {
            @Override
            protected VimeoThumbnail doInBackground(Void... voids) {
                String url = "https://vimeo.com/api/oembed.json?url=https://player.vimeo.com/video/" + videoId;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                VimeoThumbnail vimeoThumbnail = null;
                try {
                    Response response = client.newCall(request).execute();
                    vimeoThumbnail = new Gson().fromJson(response.body().string(), VimeoThumbnail.class);
                } catch (Exception e) {
                    Log.e(context.getPackageName(), e.toString());
                }
                return vimeoThumbnail;
            }

            @Override
            protected void onPostExecute(VimeoThumbnail vimeoThumbnail) {
                super.onPostExecute(vimeoThumbnail);
                if (vimeoThumbnail != null && vimeoThumbnail.thumbnailUrl != null) {
                    RequestOptions options = new RequestOptions()
                            .priority(Priority.NORMAL)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                    Glide.with(context)
                            .load(vimeoThumbnail.thumbnailUrl)
                            .apply(options)
                            .into(vimeoThumbnailImageView);
                }
            }
        }.execute();
    }
}
