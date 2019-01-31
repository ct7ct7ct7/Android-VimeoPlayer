package com.ct7ct7ct7.androidvimeoplayer.view;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ct7ct7ct7.androidvimeoplayer.R;
import com.ct7ct7ct7.androidvimeoplayer.listeners.ViemoFullscreenClickListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;

public class DefaultControlPanelView {
    private View vimeoPanelView;
    private View vimeoShadeView;
    private ImageView vimeoSettingsButton;
    private ImageView vimeoFullscreenButton;
    private SeekBar vimeoSeekBar;
    private TextView vimeoCurrentTimeTextView;
    private ImageView vimeoPlayButton;
    private ImageView vimeoPauseButton;
    private ImageView vimeoReplayButton;
    private TextView vimeoTitleTextView;
    private View controlsRootView;
    private ViewPropertyAnimator animator;
    private boolean ended = false;

    public DefaultControlPanelView(final VimeoPlayerView vimeoPlayerView) {
        View defaultControlPanelView = View.inflate(vimeoPlayerView.getContext(), R.layout.view_default_control_panel, vimeoPlayerView);
        vimeoPanelView = defaultControlPanelView.findViewById(R.id.vimeoPanelView);
        vimeoShadeView = defaultControlPanelView.findViewById(R.id.vimeoShadeView);
        vimeoSettingsButton = defaultControlPanelView.findViewById(R.id.vimeoSettingsButton);
        vimeoFullscreenButton = defaultControlPanelView.findViewById(R.id.vimeoFullscreenButton);
        vimeoSeekBar = defaultControlPanelView.findViewById(R.id.vimeoSeekBar);
        vimeoCurrentTimeTextView = defaultControlPanelView.findViewById(R.id.vimeoCurrentTimeTextView);
        vimeoPlayButton = defaultControlPanelView.findViewById(R.id.vimeoPlayButton);
        vimeoPauseButton = defaultControlPanelView.findViewById(R.id.vimeoPauseButton);
        vimeoReplayButton = defaultControlPanelView.findViewById(R.id.vimeoReplayButton);
        vimeoTitleTextView = defaultControlPanelView.findViewById(R.id.vimeoTitleTextView);
        controlsRootView = defaultControlPanelView.findViewById(R.id.controlsRootView);

        vimeoSeekBar.setVisibility(View.INVISIBLE);
        vimeoPanelView.setVisibility(View.VISIBLE);
        vimeoShadeView.setVisibility(View.VISIBLE);
        controlsRootView.setVisibility(View.GONE);

        vimeoPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vimeoPlayerView.pause();
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
            public void onReady(String title, float duration) {
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
                dismissControls(4000);
            }

            @Override
            public void onPaused(float seconds) {
                if (ended) {
                    vimeoPanelView.setBackgroundColor(Color.BLACK);
                    showControls(100, false);
                    vimeoReplayButton.setVisibility(View.VISIBLE);
                    vimeoPauseButton.setVisibility(View.GONE);
                    vimeoPlayButton.setVisibility(View.GONE);
                } else {
                    vimeoReplayButton.setVisibility(View.GONE);
                    vimeoPauseButton.setVisibility(View.GONE);
                    vimeoPlayButton.setVisibility(View.VISIBLE);
                    dismissControls(4000);
                }
            }

            @Override
            public void onEnded(float duration) {
                ended = true;
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
                showControls(100, false);
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
                showControls(300, true);
            }
        });
    }


    public void dismissControls(final int duration) {
        if (animator != null) {
            animator.cancel();
        }

        animator = controlsRootView.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        controlsRootView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        animator.removeAllListeners();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
        animator.start();
    }

    public void showControls(final int duration, final boolean autoMask) {
        if (animator != null) {
            animator.cancel();
        }

        animator = controlsRootView.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        controlsRootView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (autoMask) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissControls(2000);
                                }
                            },3000);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        animator.removeAllListeners();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
        animator.start();
    }

    public void setViemoFullscreenClickListener(final ViemoFullscreenClickListener fullscreenClickListener) {
        vimeoFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenClickListener.onClick(v);
            }
        });
    }
}
