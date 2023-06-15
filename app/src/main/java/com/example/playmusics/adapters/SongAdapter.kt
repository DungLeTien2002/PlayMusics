package com.example.playmusics.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playmusics.R
import com.example.playmusics.databinding.ItemSongBinding
import com.example.playmusics.fragments.HomeFragment
import com.example.playmusics.models.Song

class SongAdapter(val onClick:(songs:MutableList<Song>,position:Int)->Unit) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private val songs: MutableList<Song> = mutableListOf()

    inner class ViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(songs: MutableList<Song>,position: Int) {

            when (adapterPosition) {
                0 -> {
                    binding.tvRank.setTextColor(Color.parseColor("#FF6666"))
                }
                1 -> {
                    binding.tvRank.setTextColor(Color.parseColor("#33FF00"))
                }
                2 -> {
                    binding.tvRank.setTextColor(Color.parseColor("#FF9900"))
                }

                else -> {
                    binding.tvRank.setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
            //adapterPosition trả về vị trí của item trong recyclerView bắt đầu từ 0
            binding.apply {
                tvRank.text = "${adapterPosition + 1}"
                tvName.text = songs[position].name
                tvArtists.text = songs[position].artists_names

                root.setOnClickListener {
                    onClick(songs,position)

                }
            }

            Glide.with(binding.root.context).load(songs[position].thumbnail).into(binding.image)


        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(songs,position)

    }

    override fun getItemCount(): Int {
        return songs.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(newList: MutableList<Song>) {
        songs.clear()
        songs.addAll(newList)
        notifyDataSetChanged()//load lai view
    }
}