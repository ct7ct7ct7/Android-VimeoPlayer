package com.ct7ct7ct7.androidvimeoplayersample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //vimeoPlayer.initialize({YourPrivateVideoId}, "YourDomain")
        vimeoPlayer.initialize(59777392)
        lifecycle.addObserver(vimeoPlayer)

        vimeoPlayer.addTimeListener { second ->
            playerCurrentTimeTextView.text = getString(R.string.player_current_time, second.toString())
        }

        vimeoPlayer.addErrorListener { message, method, name ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        vimeoPlayer.addStateListener(object : VimeoPlayerStateListener {
            override fun onLoaded(videoId: Int) {
                playerStateTextView.text = getString(R.string.player_state, "onLoaded")
            }

            override fun onPlaying(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPlaying")
            }

            override fun onPaused(seconds: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPaused")
            }

            override fun onEnded(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onEnded")
            }

            override fun onInitFailed() {
                playerStateTextView.text = getString(R.string.player_state, "onInitFailed")
            }
        })

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
            vimeoPlayer.setTopicColor(Color.GREEN)
        }
    }
}
