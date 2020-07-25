package com.maxfour.libreplayer.adapter.song

import android.app.ActivityOptions
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.NavigationUtil
import java.util.ArrayList

open class PlaylistSongAdapter(
	activity: AppCompatActivity,
	dataSet: ArrayList<Song>,
	itemLayoutRes: Int,
	usePalette: Boolean,
	cabHolder: CabHolder?
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, usePalette, cabHolder, false) {

	init {
		this.setMultiSelectMenuRes(R.menu.menu_cannot_delete_single_songs_playlist_songs_selection)
	}

	override fun createViewHolder(view: View): SongAdapter.ViewHolder {
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
		if (holder.itemViewType == OFFSET_IT