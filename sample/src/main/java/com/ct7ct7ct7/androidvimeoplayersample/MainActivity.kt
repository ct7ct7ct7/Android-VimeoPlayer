package com.ct7ct7ct7.androidvimeoplayersample

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack
import com.ct7ct7ct7.androidvimeoplayersample.examples.FullscreenActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.MenuActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.OriginalControlsActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.RecyclerViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_white)
        toolbar.setNavigationOnClickListener { v ->
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawers()
            } else {
                drawerLayout.openDrawer(navigationView)
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fullscreenExampleItem -> {
                    startActivity(Intent(this@MainActivity, FullscreenActivity::class.java))
                    true
                }
                R.id.menuExampleItem -> {
                    startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                    true
                }
                R.id.recyclerViewExampleItem -> {
                    startActivity(Intent(this@MainActivity, RecyclerViewActivity::class.java))
                    true
                }
                R.id.originalControlsExampleItem -> {
                    startActivity(Intent(this@MainActivity, OriginalControlsActivity::class.java))
                    true
                }
            }
            false
        }
    }
    
    private fun setupView(){
        lifecycle.addObserver(vimeoPlayer)
        vimeoPlayer.initialize(59777392)
        //vimeoPlayer.initialize({YourPrivateVideoId}, "SettingsEmbeddedUrl")
        //vimeoPlayer.initialize({YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")

        vimeoPlayer.addTimeListener { second ->
            playerCurrentTimeTextView.text = getString(R.string.player_current_time, second.toString())
        }

        vimeoPlayer.addErrorListener { message, method, name ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        vimeoPlayer.addReadyListener(object : VimeoPlayerReadyListener {
            override fun onReady(title: String?, duration: Float, textTrackArray: Array<TextTrack>) {
                playerStateTextView.text = getString(R.string.player_state, "onReady")
            }

            override fun onInitFailed() {
                playerStateTextView.text = getString(R.string.player_state, "onInitFailed")
            }
        })

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
}
