package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayersample.R
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityOriginalControlsBinding

class OriginalControlsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOriginalControlsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOriginalControlsBinding.inflate(layoutInflater)
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
    }
}