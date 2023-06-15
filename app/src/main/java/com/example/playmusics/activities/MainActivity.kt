package com.example.playmusics.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.playmusics.R
import com.example.playmusics.adapters.SongAdapter
import com.example.playmusics.adapters.ViewPagerAdapter
import com.example.playmusics.api.CallApiMusics
import com.example.playmusics.databinding.ActivityMainBinding
import com.example.playmusics.fragments.HomeFragment
import com.example.playmusics.models.Song


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (binding.viewPager.currentItem) {
                    0 -> {
                        binding.btvNav.menu.findItem(R.id.action_Home).isChecked = true
                    }
                    1->{
                        binding.btvNav.menu.findItem(R.id.action_favorite).isChecked=true
                    }
                    2->{
                        binding.btvNav.menu.findItem(R.id.action_my_page).isChecked=true
                    }
                }
            }
        })

        binding.btvNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_Home -> {
                    binding.viewPager.currentItem = 0

                    toast("Home")
                    true
                }
                R.id.action_favorite -> {
                    binding.viewPager.currentItem = 1
                    toast("Favorite")
                    true
                }
                R.id.action_my_page -> {
                    binding.viewPager.currentItem = 2
                    toast("My_Page")
                    true
                }
                else -> false

            }
        }

    }






    private fun Context.toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()

    }



}