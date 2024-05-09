package com.ct7ct7ct7.androidvimeoplayersample.examples

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ItemRecyclerviewBinding
import java.util.ArrayList

class ExampleRecyclerViewAdapter(private val lifecycle: Lifecycle, val items: ArrayList<Int>) :
    RecyclerView.Adapter<ExampleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        lifecycle.addObserver(binding.vimeoPlayerView)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = items[position]
        holder.binding.vimeoPlayerView.initialize(true, id)
    }

    inner class ViewHolder(val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}