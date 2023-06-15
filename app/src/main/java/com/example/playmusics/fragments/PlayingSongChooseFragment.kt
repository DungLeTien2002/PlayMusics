package com.example.playmusics.fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.playmusics.Constants
import com.example.playmusics.R
import com.example.playmusics.databinding.FragmentPlayingSongChooseBinding
import com.example.playmusics.models.Song
import java.util.ArrayList
import androidx.lifecycle.lifecycleScope
import com.example.playmusics.activities.MainActivity
import com.example.playmusics.databinding.FragmentHomeBinding
import com.example.playmusics.databinding.PlayingSongMiniBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


@Suppress("DEPRECATION")
class PlayingSongChooseFragment : Fragment(R.layout.fragment_playing_song_choose) {
    companion object {
        var isPlaying: Boolean = true
        val mediaPlayer = MediaPlayer()
        var position=0
        var  songList:MutableList<Song> =ArrayList()
        const val updateInterval = 100

    }

    private lateinit var binding: FragmentPlayingSongChooseBinding
    //private var homeFragment:HomeFragment?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayingSongChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSongPlay()
    }


    private fun setSongPlay() {
        binding.imgBack.setOnClickListener {
            val currentFragment = fragmentManager?.findFragmentById(R.id.main)
            currentFragment?.let { fragment: Fragment ->
                fragmentManager?.beginTransaction()?.hide(fragment)?.commit()
            }


        }

         position = arguments?.getInt(Constants.song_position)!!
         songList = arguments?.getParcelableArrayList<Song>(Constants.SONG_PLAY)!!

        setMediaAndProperties(position,songList)

        binding.btnPrevious.setOnClickListener {
            position = if(position==0){
                99
            }else {
                position?.minus(1)!!
            }

            PreviousSong(position,songList)
        }
        binding.btnNext.setOnClickListener {
            position = if(position==99){
                0
            }else {
                position?.plus(1)!!
            }

            nextSong(position, songList)
        }
        //
        binding.btnPlay.setOnClickListener {
            if (isPlaying) pauseSong()
            else playSong()
        }


    }

    private fun autoNextSong(updateInterval: Int) {
        lifecycleScope.launch {
            while (true) {
                delay(updateInterval.toLong())


                if (binding.seekBar.progress == binding.seekBar.max ||binding.seekBar.progress == binding.seekBar.max?.minus(1) ) {
                    position = if (position == 99) {
                        0
                    } else {
                        position?.plus(1)!!
                    }
                    nextSong(position, songList)

                    break
                }
            }
        }
    }

    private fun PreviousSong(
        position: Int?,
        songList: MutableList<Song>
    ) {

        setMediaAndProperties(position, songList)


    }
    private fun nextSong(
        position: Int?,
        songList: MutableList<Song>
    ) {

        setMediaAndProperties(position, songList)


    }

    private fun setMediaAndProperties(
        position: Int?,
        songList: MutableList<Song>?
    ) {
        createMediaPlayer(position, songList)
        setPropertiesPlayingSong(position, songList)
        autoNextSong(updateInterval)
        putPosition()
    }

    private fun setPropertiesPlayingSong(
        position: Int?,
        songList: MutableList<Song>?
    ) {
        binding.apply {
            //songList[position].name
            nameSong.text = position?.let { songList?.get(it)?.name }
            tvAuthorSingPlay.text = position?.let { songList?.get(it)?.artists_names }
            tvNameSongPlay.text = position?.let { songList?.get(it)?.name }
            totalTime.text =
                position?.let { songList?.get(it)?.let { it -> timerConversion(it.duration) } }
            seekBar.max = position?.let { songList?.get(it)?.duration }!!
                //timerConversion(songList[position].duration)
                //position?.let { songList?.get(it)?.duration }!!
            seekBar.progress = mediaPlayer.currentPosition
        }

        Glide.with(this).load(position?.let { songList?.get(it)?.thumbnail })
            .transition(DrawableTransitionOptions.withCrossFade()).circleCrop()
            .into(binding.imgSongPlay)

        var currentPos = 0

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentPos = mediaPlayer.currentPosition
                binding.currentTime.text = timerConversion2(currentPos)
                binding.seekBar.progress = currentPos / 1000
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)

        //set seekBar.progress
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = (progress * mediaPlayer.duration) / seekBar?.max!!
                    //chuyen den phat doan nhac newPositon
                    mediaPlayer.seekTo(newPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(seekBar?.progress==seekBar?.max) {
                    Companion.position = if(Companion.position ==99){
                        0
                    }else {
                        Companion.position?.plus(1)!!
                    }

                    nextSong(Companion.position, Companion.songList)
                }



            }
        })

    }

    private fun createMediaPlayer(
        position: Int?,
        songList: MutableList<Song>?
    ) {
        val url = Uri.parse(
            "http://api.mp3.zing.vn/api/streaming/audio/${
                position?.let {
                    songList?.get(
                        it
                    )?.id
                }
            }/320"
        )

        mediaPlayer.apply {
            reset()
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setDataSource(requireContext().applicationContext, url)
            prepare()
            start()

            binding.imgSongPlay.animate().rotationBy(36000f).setDuration(1000000).start()
        }
    }


    private fun playSong() {
        binding.btnPlay.setImageResource(R.drawable.ic_play)
        mediaPlayer.start()
        isPlaying = true
    }

    private fun pauseSong() {
        binding.btnPlay.setImageResource(R.drawable.ic_pause)
        mediaPlayer.pause()
        isPlaying = false
    }


    private fun timerConversion(value: Int): String {
        var audioTime = ""
        val hrs = value / 3600
        val mns = value % 3600 / 60
        val scs = value % 60

        audioTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return audioTime
    }

    private fun timerConversion2(value: Int): String {
        var audioTime = ""
        val hrs = value / 3600000
        val mns = value / 60000 % 60000
        val scs = value % 60000 / 1000

        audioTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return audioTime
    }


}