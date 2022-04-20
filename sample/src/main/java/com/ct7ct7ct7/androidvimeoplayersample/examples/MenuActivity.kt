package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.view.menu.ViemoMenuItem
import com.ct7ct7ct7.androidvimeoplayersample.R
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
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

        binding.vimeoPlayer.setMenuVisibility(true)
        binding.vimeoPlayer.addMenuItem(
            ViemoMenuItem(
                "settings",
                R.drawable.ic_settings,
                View.OnClickListener {
                    Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
                    binding.vimeoPlayer.dismissMenuItem()
                })
        )
        binding.vimeoPlayer.addMenuItem(
            ViemoMenuItem(
                "star",
                R.drawable.ic_star,
                View.OnClickListener {
                    Toast.makeText(this, "star clicked", Toast.LENGTH_SHORT).show()
                    binding.vimeoPlayer.dismissMenuItem()
                })
        )
    }
}