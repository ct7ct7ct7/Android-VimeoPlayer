package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ct7ct7ct7.androidvimeoplayersample.R
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityRecyclerviewBinding
import java.util.ArrayList


class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
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
        val ids: ArrayList<Int> = arrayListOf()
        ids.add(56282283)
        ids.add(318380844)
        ids.add(318173011)
        ids.add(318794329)
        ids.add(315632203)
        ids.add(19231868)

        val adapter = TestRecyclerViewAdapter(lifecycle, ids)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
        binding.recyclerView.adapter = adapter
    }
}