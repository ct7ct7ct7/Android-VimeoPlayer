package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.ct7ct7ct7.androidvimeoplayer.view.menu.ViemoMenuItem
import com.ct7ct7ct7.androidvimeoplayersample.R
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
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

        vimeoPlayer.setMenuVisibility(true)
        vimeoPlayer.addMenuItem(ViemoMenuItem("settings", R.drawable.ic_settings, View.OnClickListener {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
            vimeoPlayer.dismissMenuItem()
        }))
        vimeoPlayer.addMenuItem(ViemoMenuItem("star", R.drawable.ic_star, View.OnClickListener {
            Toast.makeText(this, "star clicked", Toast.LENGTH_SHORT).show()
            vimeoPlayer.dismissMenuItem()
        }))
    }
}