package com.maxfour.libreplayer.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialcab.MaterialCab
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.base.AbsSlidingMusicPanelActivity
import com.maxfour.libreplayer.adapter.song.ShuffleButtonSongAdapter
import com.maxfour.libreplayer.extensions.applyToolbar
import com.maxfour.libreplayer.helper.menu.GenreMenuHelper
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Genre
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.mvp.presenter.GenreDetailsPresenter
import com.maxfour.libreplayer.mvp.presenter.GenreDetailsView
import com.maxfour.libreplayer.util.DensityUtil
import com.maxfour.libreplayer.util.PlayerColorUtil
import com.maxfour.libreplayer.util.ViewUtil
import kotlinx.android.synthetic.main.activity_playlist_detail.*
import java.util.ArrayList
import javax.inject.Inject

class GenreDetailsActivity : AbsSlidingMusicPanelActivity(), CabHolder, GenreDetailsView {

	@Inject
	lateinit var genreDetailsPresenter: GenreDetailsPresenter

	private lateinit var genre: Genre
	private lateinit var songAdapter: ShuffleButtonSongAdapter
	private var cab: MaterialCab? = null

	private fun getEmojiByUnicode(unicode: Int): String {
		return String(Character.toChars(unicode))
	}

	private fun checkIsEmpty() {
		checkForPadding()
		emptyEmoji.text = getEmojiByUnicode(0x1F631)
		empty?.visibility = if (songAdapter.itemCount == 0) View.VISIBLE else View.GONE
	}

	private fun checkForPadding() {
		va