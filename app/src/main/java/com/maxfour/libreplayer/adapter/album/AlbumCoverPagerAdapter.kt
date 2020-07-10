package com.maxfour.libreplayer.adapter.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.fragments.AlbumCoverStyle
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.misc.CustomFragmentStatePagerAdapter
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import java.util.ArrayList

class AlbumCoverPagerAdapter(
	fm: FragmentManager,
	private val dataSet: ArrayList<Song>
) : CustomFragmentStatePagerAdapter(fm) {

	private var currentColorReceiver: AlbumCoverFragment.ColorReceiver? = null
	private var currentColorReceiverPosition = -1

	override fun getItem(position: Int): Fragment {
		return Al