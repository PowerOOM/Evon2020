package com.maxfour.libreplayer.adapter.song

import android.app.ActivityOptions
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialcab.MaterialCab
import com.bumptech.glide.Glide
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.base.AbsMultiSelectAdapter
import com.maxfour.libreplayer.adapter.base.MediaEntryViewHolder
import com.maxfour.libreplayer.extensions.hide
import com.maxfour.libreplayer.extensions.show
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.SortOrder
import com.maxfour.libreplayer.helper.menu.SongMenuHelper
import com.maxfour.libreplayer.helper.menu.SongsMenuHelper
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import java.util.ArrayList

open class SongAdapter(
	protected val activity: AppCompatActivity,
	dataSet: ArrayList<Song>,
	protected var itemLayoutRes: Int,
	usePalette: Boolean,
	cabHolder: CabHolder?,
	showSectionName: Boolean = true
) : AbsMultiSelectAdapter<SongAdapter.ViewHolder, Song>(
	activity, cabHolder, R.menu.menu_media_selection
), MaterialCab.Callback, FastScrollRecyclerView.SectionedAdapter {

	var dataSet: ArrayList<Song>

	protected var usePalette = false
	private var showSectionName = true

	init {
		this.dataSet = dataSet
		this.usePalette = usePalette
		this.showSectionName = showSectionName
		this.setHasStableIds(true)
	}

	open fun swapDataSet(dataSet: ArrayList<Song>) {
		this.dataSet = dataSet
		notifyDataSetChanged()
	}

	open fun usePalette(usePalette: Boolean) {
		this.usePalette = usePalette
		notifyDataSetChanged()
	}

	override fun getItemId(position: Int): Long {
		return dataSet[position].id.toLong()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
		return createViewHolder(view)
	}

	protected open fun createViewHolder(view: View): ViewHolder {
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val song = dataSet[position]
		val isChecked = isChecked(song)
		holder.itemView.isActivated = isChecked
		if (isChecked) {
			holder.menu?.hide()
		} else {
			holder.menu?.show()
		}
		holder.title?.text = getSongTitle(song)
		holder.text?.text = getSongText(song)
		loadAlbumCover(song, holder)
	}

	private fun setColors(color: Int, holder: ViewHolder) {
		if (holder.paletteColorContainer != null) {
			holder.paletteColorContainer?.setBackgroundColor(color)
			//holder.title?.setTextColor(MaterialValueHelper.getPrimaryTextColor(activity, C