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
	) : super(activity, dataSet, itemLayoutRes, usePalette, cabHolder)

	constructor(
			activity: AppCompatActivity,
			dataSet: ArrayList<Song>, @LayoutRes itemLayoutRes: Int,
			usePalette: Boolean,
			cabHolder: CabHolder?,
			showSectionName: Boolean
	) : super(activity, dataSet, itemLayoutRes, usePalette, cabHolder, showSectionName) {
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
		if (viewType == OFFSET_ITEM) {
			val view = LayoutInflater.from(activity)
				.inflate(R.layout.item_list_quick_actions, parent, false)
			return createViewHolder(view)
		}
		return super.onCreateViewHolder(parent, viewType)
	}

	override fun createViewHolder(view: View): SongAdapter.ViewHolder {
		return ViewHolder(view)
	}

	override fun getItemId(position: Int): Long {
		var positionFinal = position
		positionFinal--
		return if (positionFinal < 0) -2 else super.getItemId(positionFinal)
	}

	override fun getIdentifier(position: Int): Song? {
		var positionFinal = position
		positionFinal--
		return if (positionFinal < 0) null else super.getIdentifier(positionFinal)
	}

	override fun getItemCount(): Int {
		val superItemCount = super.getItemCount()
		return if (superItemCount == 0) 0 else superItemCount + 1
	}

	