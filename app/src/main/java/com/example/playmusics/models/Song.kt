package com.example.playmusics.models

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Song(
    val artists: List<Artists>,
    val artist:Artist,
    val album: Album,
    val id: String,
    val name:String,
    val title:String,
    val code:String,
    val content_owner:Int,
    val isoffical:Boolean,
    val isWorldWide:Boolean,
    val playlist_id:String,
    val artists_names :String,
    val performer:String,
    val type:String,
    val link:String,
    val lyric:String,
    val thumbnail:String,
    val duration:Int,
    val total:Int,
    val rank_num:String,
    val rank_status:String,
    val position:String,
    val order:String,



):Parcelable
