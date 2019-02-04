package com.ct7ct7ct7.androidvimeoplayersample

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        vimeoPlayer.initialize(59777392)
        //vimeoPlayer.initialize({YourPrivateVideoId}, "SettingsEmbeddedUrl")
        //vimeoPlayer.initialize({YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")

        lifecycle.addObserver(vimeoPlayer)

        vimeoPlayer.addTimeListener { second ->
            playerCurrentTimeTextView.text = getString(R.string.player_current_time, second.toString())
        }

        vimeoPlayer.addErrorListener { message, method, name ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        vimeoPlayer.addStateListener(object : VimeoPlayerStateListener {
            override fun onPlaying(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPlaying")
            }

            override fun onPaused(seconds: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPaused")
            }

            override fun onEnded(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onEnded")
            }
        })

        vimeoPlayer.setFullscreenClickListener {
            vimeoPlayer.pause()
            startActivityForResult(VimeoPlayerActivity.createIntent(this, vimeoPlayer), REQUEST_CODE)
        }

        vimeoPlayer.setMenuClickListener {
            //TODO
        }

        volumeSeekBar.progress = 100
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var volume = progress.toFloat() / 100
                vimeoPlayer.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        vimeoPlayer.addVolumeListener { volume ->
            playerVolumeTextView.text = getString(R.string.player_volume, volume.toString())
        }

        playButton.setOnClickListener {
            vimeoPlayer.play()
        }

        pauseButton.setOnClickListener {
            vimeoPlayer.pause()
        }

        getCurrentTimeButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.player_current_time, vimeoPlayer.currentTimeSeconds.toString()), Toast.LENGTH_LONG).show()
        }

        loadVideoButton.setOnClickListener {
            vimeoPlayer.loadVideo(19231868)
        }

        colorButton.setOnClickListener {
            vimeoPlayer.topicColor = Color.GREEN
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            vimeoPlayer.seekTo(playAt)

            var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))
            when (playerState) {
                PlayerState.PLAYING -> vimeoPlayer.play()
                PlayerState.PAUSED -> vimeoPlayer.pause()
            }
        }
    }
}
