package com.ct7ct7ct7.androidvimeoplayersample

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityMainBinding
import com.ct7ct7ct7.androidvimeoplayersample.examples.FullscreenActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.MenuActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.OriginalControlsActivity
import com.ct7ct7ct7.androidvimeoplayersample.examples.RecyclerViewActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu_white)
        binding.toolbar.setNavigationOnClickListener { v ->
            if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
                binding.drawerLayout.closeDrawers()
            } else {
                binding.drawerLayout.openDrawer(binding.navigationView)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fullscreenExampleItem -> {
                    startActivity(Intent(this@MainActivity, FullscreenActivity::class.java))
                }
                R.id.menuExampleItem -> {
                    startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                }
                R.id.recyclerViewExampleItem -> {
                    startActivity(Intent(this@MainActivity, RecyclerViewActivity::class.java))
                }
                R.id.originalControlsExampleItem -> {
                    startActivity(Intent(this@MainActivity, OriginalControlsActivity::class.java))
                }
            }
            false
        }
    }

    private fun setupView() {
        lifecycle.addObserver(binding.vimeoPlayerView)
        binding.vimeoPlayerView.initialize(true, 59777392)
        //vimeoPlayerView.initialize(true, {YourPrivateVideoId}, "SettingsEmbeddedUrl")
        //vimeoPlayerView.initialize(true, {YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")

        binding.vimeoPlayerView.addTimeListener { second ->
            binding.playerCurrentTimeTextView.text =
                getString(R.string.player_current_time, second.toString())
        }

        binding.vimeoPlayerView.addErrorListener { message, method, name ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        binding.vimeoPlayerView.addReadyListener(object : VimeoPlayerReadyListener {
            override fun onReady(
                title: String?,
                duration: Float,
                textTrackArray: Array<TextTrack>
            ) {
                binding.playerStateTextView.text = getString(R.string.player_state, "onReady")
            }

            override fun onInitFailed() {
                binding.playerStateTextView.text = getString(R.string.player_state, "onInitFailed")
            }
        })

        binding.vimeoPlayerView.addStateListener(object : VimeoPlayerStateListener {
            override fun onPlaying(duration: Float) {
                binding.playerStateTextView.text = getString(R.string.player_state, "onPlaying")
            }

            override fun onPaused(seconds: Float) {
                binding.playerStateTextView.text = getString(R.string.player_state, "onPaused")
            }

            override fun onEnded(duration: Float) {
                binding.playerStateTextView.text = getString(R.string.player_state, "onEnded")
            }
        })

        binding.volumeSeekBar.progress = 100
        binding.volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var volume = progress.toFloat() / 100
                binding.vimeoPlayerView.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.vimeoPlayerView.addVolumeListener { volume ->
            binding.playerVolumeTextView.text = getString(R.string.player_volume, volume.toString())
        }

        binding.playButton.setOnClickListener {
            binding.vimeoPlayerView.play()
        }

        binding.pauseButton.setOnClickListener {
            binding.vimeoPlayerView.pause()
        }

        binding.getCurrentTimeButton.setOnClickListener {
            Toast.makeText(
                this,
                getString(
                    R.string.player_current_time,
                    binding.vimeoPlayerView.currentTimeSeconds.toString()
                ),
                Toast.LENGTH_LONG
            ).show()
        }

        binding.loadVideoButton.setOnClickListener {
            binding.vimeoPlayerView.loadVideo(19231868)
        }

        binding.colorButton.setOnClickListener {
            binding.vimeoPlayerView.topicColor = Color.GREEN
        }
    }
}
