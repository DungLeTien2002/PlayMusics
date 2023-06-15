package com.example.playmusics.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.playmusics.Constants
import com.example.playmusics.R
import com.example.playmusics.adapters.SongAdapter
import com.example.playmusics.databinding.FragmentHomeBinding
import com.example.playmusics.databinding.PlayingSongMiniBinding
import com.example.playmusics.models.Song
import com.example.playmusics.viewModel.MusicTopViewModel
import org.greenrobot.eventbus.EventBus
import java.text.FieldPosition

private const val TAG = "HomeFragment"

@Suppress("DEPRECATION")

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val songAdapter by lazy { SongAdapter(::onClick) }
    private var songString: String = ""
     lateinit var binding: FragmentHomeBinding
    var isFragment: Boolean = false
    private val contactAdapter by lazy {
        SongAdapter(::onClick)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        binding.btvPlayingSongMn.visibility = View.GONE
    }


    private fun initRecyclerView() {
        binding.rcvHome.adapter = songAdapter
        binding.rcvHome.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val musicTopViewModel: MusicTopViewModel =
            ViewModelProvider(this)[MusicTopViewModel::class.java]
        musicTopViewModel.getTopSongFromAPI()

        musicTopViewModel.mSongTopLiveData.observe(viewLifecycleOwner) {
            songAdapter.setDataList(it)
        }
    }

    private fun onClick(songs: MutableList<Song>, position: Int) {
        //someMethod(songs[position])
        //false chua hien PlayingSongChoose


        if (isFragment) {


            if (songString == songs[position].id || songString == "") {
                //visible
                val currentFragment = fragmentManager?.findFragmentById(R.id.main)
                currentFragment?.let { fragment: Fragment ->
                    fragmentManager?.beginTransaction()?.show(fragment)?.commit()
                }

            } else {
                //visible set lai data
                val fragment = put(songs, position)
                replaceFragment(fragment, songs[position])
                setDataMiniPlaySong(songs[position])
            }

        } else {
            //set lai data
            binding.btvPlayingSongMn.visibility = View.VISIBLE
            val fragment = put(songs, position)
            setDataMiniPlaySong(songs[position])
            replaceFragment(fragment, songs[position])


        }


    }


    private fun put(songs: MutableList<Song>, position: Int): PlayingSongChooseFragment {
        val fragment = PlayingSongChooseFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(Constants.SONG_PLAY, ArrayList(songs))
                putInt(Constants.song_position, position)
            }
        }
        return fragment
    }


    private fun replaceFragment(
        fragment: PlayingSongChooseFragment,
        song: Song
    ) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragment)
        transaction.commit()
        isFragment = true
        songString = song.id

    }

    private fun setDataMiniPlaySong(song: Song) {
        val inflatedView = LayoutInflater.from(requireContext()).inflate(R.layout.playing_song_mini, null)
        val miniBinding: PlayingSongMiniBinding = PlayingSongMiniBinding.bind(inflatedView)
        Glide.with(this).load(song.thumbnail)
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop().into(miniBinding.imgImageAvtMn)
        miniBinding.apply {
            tvNameSongMn.text = song.name
            tvAuthorNameMn.text = song.artists_names
        }
        addViewMiniPlayingSong(inflatedView)
        inflatedView.setOnClickListener{
            val currentFragment = fragmentManager?.findFragmentById(R.id.main)
            currentFragment?.let { fragment: Fragment ->
                fragmentManager?.beginTransaction()?.show(fragment)?.commit()
            }
        }
    }
    private fun addViewMiniPlayingSong(view: View) {

        binding.btvPlayingSongMn.removeAllViews()
        binding.btvPlayingSongMn.addView(view)

    }



}


