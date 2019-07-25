package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
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
      //  Toast.makeText(this,"OnCreateCalled",Toast.LENGTH_SHORT).show()
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
            var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE

            //Log.d("FullscreenActivity",""+vimeoPlayer.currentTimeSeconds)
            //vimeoPlayer.seekTo(vimeoPlayer.currentTimeSeconds)
            startActivityForResult(VimeoPlayerActivity.createIntent(this, requestOrientation, vimeoPlayer), REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            vimeoPlayer.seekTo(playAt)
                  //  Toast.makeText(this,"CurrentTime"+playAt,Toast.LENGTH_SHORT).show()

            var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))
            //Log.e("PlayerState",""+playerState)
            when (playerState) {
                PlayerState.PLAYING -> vimeoPlayer.pause()
                PlayerState.PAUSED -> vimeoPlayer.pause()
                PlayerState.UNKNOWN -> vimeoPlayer.pause()

            }
        }
    }
}