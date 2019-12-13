package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.ct7ct7ct7.androidvimeoplayersample.R
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : AppCompatActivity() {
    var REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupView() {
        lifecycle.addObserver(vimeoPlayer)
        vimeoPlayer.initialize(true, 59777392)
        vimeoPlayer.setFullscreenVisibility(true)

        vimeoPlayer.setFullscreenClickListener {
            val requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO
            startActivityForResult(VimeoPlayerActivity.createIntent(this, requestOrientation, vimeoPlayer), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            vimeoPlayer.seekTo(playAt)

            when (PlayerState.valueOf(data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))) {
                PlayerState.PLAYING -> vimeoPlayer.play()
                PlayerState.PAUSED -> vimeoPlayer.pause()
                else -> {
                }
            }
        }
    }
}