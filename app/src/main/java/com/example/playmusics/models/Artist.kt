package com.example.playmusics.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id:String,
    val name:String,
    val link:String,
    val cover:String,
    val thumbnail:String
):Parcelable
