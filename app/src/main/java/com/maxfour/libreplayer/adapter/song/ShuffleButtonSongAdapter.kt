package com.maxfour.libreplayer.adapter.song

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Song
import java.util.*

class ShuffleButtonSongAdapter(
		activity: AppCompatActivity,
		dataSet: ArrayList<Song>,
		itemLayoutRes: Int,
		usePalette: Boolean,
		cabHolder: CabHolder?
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, usePalette, cabHolder) {

	override fun createViewHolder(view: View): SongAdapter.ViewHolder {
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
		if (holder.itemViewType == OFFSET_ITEM) {
			val viewHolder = holder as ViewHolder
			viewHolder.playAction?.let {
				it.setOnClickListener {
					MusicPlayerRemote.openQueue(dataSet, 0, true)
				}
			}
			viewHolder.shuffleAction?.let {
				it.setOnClickListener {
					MusicPlayerRe