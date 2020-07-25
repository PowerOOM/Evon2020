package com.maxfour.libreplayer.adapter.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Song
import java.util.*

abstract class AbsOffsetSongAdapter : SongAdapter {

	constructor(
			activity: AppCompatActivity,
			dataSet: ArrayList<Song>, @LayoutRes itemLayoutRes: Int,
			usePalette: Boolean,
			cabHolder: CabHolder?
	) : super(ac