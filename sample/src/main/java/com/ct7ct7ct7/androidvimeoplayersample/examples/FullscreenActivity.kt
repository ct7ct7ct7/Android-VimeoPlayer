package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.ct7ct7ct7.androidvimeoplayersample.R
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding

    var REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupView() {
        lifecycle.addObserver(binding.vimeoPlayer)
        binding.vimeoPlayer.initialize(true, 59777392)
        binding.vimeoPlayer.setFullscreenVisibility(true)

        binding.vimeoPlayer.setFullscreenClickListener {
            var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO
            startActivityForResult(VimeoPlayerActivity.createIntent(this, requestOrientation, binding.vimeoPlayer), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            binding.vimeoPlayer.seekTo(playAt)

            var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))
            when (playerState) {
                PlayerState.PLAYING -> binding.vimeoPlayer.play()
                PlayerState.PAUSED -> binding.vimeoPlayer.pause()
            }
        }
    }
}