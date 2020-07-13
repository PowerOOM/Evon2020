package com.maxfour.libreplayer.adapter.album

import android.app.Activity
import android.app.ActivityOptions
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.views.MetalRecyclerViewPager

class AlbumFullWidthAdapter(
	private val activity: Activity,
	private val dataSet: ArrayList<Album>,
	metrics: DisplayMetrics
) : MetalRecyclerViewPager.MetalAdapter<AlbumFullWidthAdapter.FullMetalViewHolder>(metrics) {

	override fun onCreateViewHolder(parent