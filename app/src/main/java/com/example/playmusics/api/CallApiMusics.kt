package com.example.playmusics.api

import com.example.playmusics.models.Music
import com.example.playmusics.models.Song
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CallApiMusics {

    @GET("xhr/chart-realtime")
    fun getSong():Call<Music>

    //https://mp3.zing.vn/xhr/chart-realtime
companion object Call1{
    val api= Retrofit.Builder()
        .baseUrl("https://mp3.zing.vn/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CallApiMusics::class.java)
}

}