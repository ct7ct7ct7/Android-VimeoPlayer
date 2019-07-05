package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ct7ct7ct7.androidvimeoplayersample.R
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.android.synthetic.main.item_recyclerview.view.*
import java.util.ArrayList


class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
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
        val ids: ArrayList<Int> = arrayListOf()
        ids.add(56282283)
        ids.add(318380844)
        ids.add(318173011)
        ids.add(318794329)
        ids.add(315632203)
        ids.add(19231868)

        val adapter = TestRecyclerViewAdapter(lifecycle, ids)
        recyclerView.layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
        recyclerView.adapter = adapter
    }


    private class TestRecyclerViewAdapter(val lifecycle: Lifecycle, val items: ArrayList<Int>) : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var id = items[position]
            holder.vimeoPlayer.initialize(true, id)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
            lifecycle.addObserver(view.vimeoPlayer)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val vimeoPlayer = view.vimeoPlayer
        }
    }
}