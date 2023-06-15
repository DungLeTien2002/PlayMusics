package com.example.playmusics.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val artists: List<Artists>,
    val id:String,
    val link:String,
    val title:String,
    val name:String,
    val ifoffical:Boolean,
    val artists_name:String,
    val thumbnail:String,
    val thumbnail_medium:String
):Parcelable
