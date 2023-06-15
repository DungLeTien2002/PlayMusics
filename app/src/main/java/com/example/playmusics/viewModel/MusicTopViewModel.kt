package com.example.playmusics.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playmusics.models.Song
import com.example.playmusics.repository.SongRepository

class MusicTopViewModel : ViewModel() {
    val mSongTopLiveData: MutableLiveData<MutableList<Song>> = MutableLiveData()

    fun getTopSongFromAPI() {
        SongRepository.getInstance().getTopMusic { isSuccess, response ->
            if (isSuccess) {
                mSongTopLiveData.postValue(response?.data?.song as? MutableList<Song>?)

            }
        }
    }
}