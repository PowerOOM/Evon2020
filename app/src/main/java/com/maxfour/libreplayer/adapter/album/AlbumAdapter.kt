package com.maxfour.libreplayer.adapter.album

import android.app.ActivityOptions
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.base.AbsMultiSelectAdapter
import com.maxfour.libreplayer.adapter.base.MediaEntryViewHolder
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.SortOrder
import com.maxfour.libreplayer.helper.menu.SongsMenuHelper
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

open class AlbumAdapter(
	protected val activity: AppCompatActivity,
	dataSet: ArrayList<Album>,
	protected var itemLayoutRes: Int,
	usePalette: Boolean,
	cabHolder: CabHolder?
) : AbsMultiSelectAdapter<AlbumAdapter.ViewHolder, Album>(
	activity,
	cabHolder,
	R.menu.menu_media_selection
), FastScrollRecyclerView.SectionedAdapter {

	var dataSet: ArrayList<Album>
		protected set

	protected var usePalette = false

	init {
		this.dataSet = dataSet
		this.usePalette = usePalette
		this.setHasStableIds(true)
	}

	fun useItemLayout(itemLayoutRes: Int) {
		this.itemLayoutRes = itemLayoutRes
		notifyDataSetChanged()
	}

	fun usePalette(usePalette: Boolean) {
		this.usePalette = usePalette
		notifyDataSetChanged()
	}

	fun swapDataSet(dataSet: ArrayList<Album>) {
		this.dataSet = dataSet
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
		return createViewHolder(view, viewType)
	}

	protected open fun createViewHolder(view: View, viewType: Int): ViewHolder {
		return ViewHolder(view)
	}

	private fun getAlbumTitle(album: Album): String? {
		return album.title
	}

	protected open fun getAlbumText(album: Album): String? {
		return album.artistName
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val album = dataSet[position]
		val isChecked = isChecked(album)
		holder.itemView.isActivated = isChecked
		holder.title?.text = getAlbumTitle(album)
		holder.text?.text = getAlbumText(album)
		holder.playSongs?.setOnClickListener {
			album.songs?.let { songs ->
				MusicPlayerRemote.openQueue(
						songs,
						0,
						true
				)
			}
		}
		loadAlbumCover(album, holder)
	}

	protected open fun setColors(color: Int, holder: ViewHolder) {
		if (holder.paletteColorContainer != null) {
			holder.title?.setTextColor(
					MaterialValueHelper.getPrimaryTextColor(
							activity,
							ColorUtil.isColorLight(color)
					)
			)
			holder.text?.setTextColor(
					MaterialValueHelper.getSecondaryTextColor(
							activity,
							ColorUtil.isColorLight(color)
					)
			)
			holder.paletteColorContainer?.setBackgroundColor(color)
		}
		holder.mask?.backgroundTintList = ColorStateList.valueOf(color)
	}

	protected open fun loadAlbumCover(album: Album, holder: ViewHolder) {
		if (holder.i