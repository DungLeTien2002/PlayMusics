package com.example.playmusics.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artists(
    val name:String,
    val link:String
):Parcelable
