
package com.maxfour.libreplayer.adapter.song

import android.graphics.Color
import android.graphics.PorterDuff.Mode
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem
import com.h6ah4i.android.widget.advrecyclerview.swipeable.annotation.SwipeableItemResults
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.MusicPlayerRemote.isPlaying
import com.maxfour.libreplayer.helper.MusicPlayerRemote.playNextSong
import com.maxfour.libreplayer.helper.MusicPlayerRemote.removeFromQueue
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.ViewUtil
import java.util.ArrayList

class PlayingQueueAdapter(
	activity: AppCompatActivity,
	dataSet: ArrayList<Song>,
	private var current: Int,
	itemLayoutRes: Int
) : SongAdapter(
	activity, dataSet, itemLayoutRes, false, null
), DraggableItemAdapter<PlayingQueueAdapter.ViewHolder>, SwipeableItemAdapter<PlayingQueueAdapter.ViewHolder> {

	private var color = -1
	private var songToRemove: Song? = null

	override fun createViewHolder(view: View): SongAdapter.ViewHolder {
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
		super.onBindViewHolder(holder, position)
		holder.imageText?.text = (position - current).toString()
		holder.time?.text = MusicUtil.getReadableDurationString(dataSet[position].duration)
		if (holder.itemViewType == HISTORY || holder.itemViewType == CURRENT) {
			setAlpha(holder, 0.5f)
		}
		if (usePalette) {
			setColor(holder, Color.WHITE)
		}
	}

	private fun setColor(holder: SongAdapter.ViewHolder, white: Int) {

		if (holder.title != null) {
			holder.title!!.setTextColor(white)
			if (color != -1) {
				holder.title!!.setTextColor(color)
			}
		}

		holder.text?.setTextColor(white)
		holder.time?.setTextColor(white)
		holder.imageText?.setTextColor(white)
		if (holder.menu != null) {
			(holder.menu as ImageView).setColorFilter(white, Mode.SRC_IN)
		}
	}

	override fun usePalette(usePalette: Boolean) {
		super.usePalette(usePalette)
		this.usePalette = usePalette
		notifyDataSetChanged()
	}

	override fun getItemViewType(position: Int): Int {
		if (position < current) {
			return HISTORY
		} else if (position > current) {
			return UP_NEXT
		}
		return CURRENT
	}

	override fun loadAlbumCover(song: Song, holder: SongAdapter.ViewHolder) {
		// We don't want to load it in this adapter
	}

	fun swapDataSet(dataSet: ArrayList<Song>, position: Int) {
		this.dataSet = dataSet
		current = position
		notifyDataSetChanged()
	}

	fun setCurrent(current: Int) {