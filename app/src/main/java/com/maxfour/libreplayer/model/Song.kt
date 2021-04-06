package com.maxfour.libreplayer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Song(
        val id: Int,
        val title: String,
        val songNumber: Int,
        val year: Int,
        val