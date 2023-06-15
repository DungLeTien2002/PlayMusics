package com.example.playmusics.repository

import android.util.Log
import com.example.playmusics.api.CallApiMusics
import com.example.playmusics.models.Music
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongRepository {
    companion object {
        private var INSTANCE: SongRepository? = null
        fun getInstance() = INSTANCE
            ?: SongRepository().also {
                INSTANCE = it
            }
    }

    fun getTopMusic(result: (isSuccess: Boolean, response: Music?) -> Unit) {
        CallApiMusics.api.getSong().enqueue(object : Callback<Music> {
            override fun onResponse(call: Call<Music>, response: Response<Music>) {

                if (response.isSuccessful) {//call API Chart Realtime

                    result(true, response.body())

                } else {
                    result(false, null)
                }
            }

            override fun onFailure(call: Call<Music>, t: Throwable) {
                Log.e("hehehehe", "hehewhw")
            }

        })


    }
}