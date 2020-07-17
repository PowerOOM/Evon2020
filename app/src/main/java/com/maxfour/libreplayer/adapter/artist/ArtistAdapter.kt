package com.maxfour.libreplayer.adapter.artist

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
import com.maxfour.libreplayer.glide.ArtistGlideRequest
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.helper.menu.SongsMenuHelper
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Artist
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.NavigationUtil
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import java.util.ArrayList

class ArtistAdapter(
	val activity: AppCompatActivity,
	var dataSet: ArrayList<Artist>,
	var itemLayoutRes: Int,
	var usePalette: Boolean,
	cabHolder: CabHolder?
) : AbsMultiSelectAdapter<ArtistAdapter.ViewHolder, Artist>(
	activity, cabHolder, R.menu.menu_media_selection
), FastScrollRecyclerView.SectionedAdapter {

	fun swapDataSet(dataSet: ArrayList<Artist>) {
		this.dataSet = dataSet
		notifyDataSetChanged()
	}

	fun usePalette(usePalette: Boolean) {
		this.usePalette = usePalette
		notifyDataSetChanged()
	}

	override fun getItemId(position: Int): Long {
		return dataSet[position].id.toLong()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(activity).inflate(itemLayoutRes, pare