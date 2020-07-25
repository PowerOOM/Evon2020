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

	ope