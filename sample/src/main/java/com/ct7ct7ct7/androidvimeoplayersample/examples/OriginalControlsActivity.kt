package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayersample.R
import kotlinx.android.synthetic.main.activity_original_controls.*

class OriginalControlsActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_original_controls)
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
        vimeoPlayer.initialize(59777392)
    }
}