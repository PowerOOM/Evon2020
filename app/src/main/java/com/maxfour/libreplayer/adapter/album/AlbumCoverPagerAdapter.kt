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
		return AlbumCoverFragment.newInstance(dataSet[position])
	}

	override fun getCount(): Int {
		return dataSet.size
	}

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val o = super.instantiateItem(container, position)
		if (currentColorReceiver != null && currentColorReceiverPosition == position) {
			receiveColor(currentColorReceiver!!, currentColorReceiverPosition)
		}
		return o
	}

	/**
	 * Only the latest passed [AlbumCoverFragment.ColorReceiver] is guaranteed to receive a
	 * response
	 */
	fun receiveColor(colorReceiver: AlbumCoverFragment.ColorReceiver, position: Int) {

		if (getFragment(position) is AlbumCoverFragment) {
			val fragment = getFragment(position) as AlbumCoverFragment
			currentColorReceiver = null
			currentColorReceiverPosition = -1
			fragment.receiveColor(colorReceiver, position)
		} else {
			currentColorReceiver = colorReceiver
			currentColorReceiverPosition = position
		}
	}

	class AlbumCoverFragment : Fragment() {

		lateinit var albumCover: ImageView
		private var isColorReady: Boolean = false
		private var color: Int = 0
		private lateinit var song: Song
		private var colorReceiver: ColorReceiver? = null
		private var request: Int = 0

		private 