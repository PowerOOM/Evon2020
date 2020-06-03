
package com.maxfour.libreplayer.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialcab.MaterialCab
import com.bumptech.glide.Glide
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.MaterialUtil
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.base.AbsSlidingMusicPanelActivity
import com.maxfour.libreplayer.activities.tageditor.AbsTagEditorActivity
import com.maxfour.libreplayer.activities.tageditor.AlbumTagEditorActivity
import com.maxfour.libreplayer.adapter.album.HorizontalAlbumAdapter
import com.maxfour.libreplayer.adapter.song.SimpleSongAdapter
import com.maxfour.libreplayer.dialogs.AddToPlaylistDialog
import com.maxfour.libreplayer.dialogs.DeleteSongsDialog
import com.maxfour.libreplayer.extensions.ripAlpha
import com.maxfour.libreplayer.extensions.show
import com.maxfour.libreplayer.glide.ArtistGlideRequest
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.SortOrder.AlbumSongSortOrder
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.model.Artist
import com.maxfour.libreplayer.mvp.presenter.AlbumDetailsPresenter
import com.maxfour.libreplayer.mvp.presenter.AlbumDetailsView
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PlayerColorUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album_content.*
import java.util.*
import javax.inject.Inject
import android.util.Pair as UtilPair

class AlbumDetailsActivity : AbsSlidingMusicPanelActivity(), AlbumDetailsView, CabHolder {
	override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
		cab?.let {
			if (it.isActive) it.finish()
		}
		cab = MaterialCab(this, R.id.cab_stub)
			.setMenu(menuRes)
			.setCloseDrawableRes(R.drawable.ic_close_white_24dp)
			.setBackgroundColor(
				PlayerColorUtil.shiftBackgroundColorForLightText(
					ATHUtil.resolveColor(
						this,
						R.attr.colorSurface
					)
				)
			)
			.start(callback)
		return cab as MaterialCab
	}

	private lateinit var simpleSongAdapter: SimpleSongAdapter
	private lateinit var album: Album
	private lateinit var artistImage: ImageView
	private var cab: MaterialCab? = null
	private val savedSortOrder: String
		get() = PreferenceUtil.getInstance(this).albumDetailSongSortOrder

	override fun createContentView(): View {
		return wrapSlidingMusicPanel(R.layout.activity_album)
	}

	@Inject
	lateinit var albumDetailsPresenter: AlbumDetailsPresenter

	private fun windowEnterTransition() {
		val slide = Slide()
		slide.excludeTarget(R.id.appBarLayout, true)
		slide.excludeTarget(R.id.status_bar, true)
		slide.excludeTarget(android.R.id.statusBarBackground, true)
		slide.excludeTarget(android.R.id.navigationBarBackground, true)

		window.enterTransition = slide
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		setDrawUnderStatusBar()
		super.onCreate(savedInstanceState)
		toggleBottomNavigationView(true)
		setStatusbarColorAuto()
		setNavigationbarColorAuto()
		setTaskDescriptionColorAuto()
		setLightNavigationBar(true)
		window.sharedElementsUseOverlay = true

		App.musicComponent.inject(this)
		albumDetailsPresenter.attachView(this)

		if (intent.extras!!.containsKey(EXTRA_ALBUM_ID)) {
			intent.extras?.getInt(EXTRA_ALBUM_ID)?.let {
				albumDetailsPresenter.loadAlbum(it)
				albumCoverContainer?.transitionName = "${getString(R.string.transition_album_art)}_$it"
			}
		} else {
			finish()
		}

		windowEnterTransition()
		ActivityCompat.postponeEnterTransition(this)


		artistImage = findViewById(R.id.artistImage)

		setupRecyclerView()

		artistImage.setOnClickListener {
			val artistPairs = ActivityOptions.makeSceneTransitionAnimation(
					this,
					UtilPair.create(
							artistImage,
							"${getString(R.string.transition_artist_image)}_${album.artistId}"
					)
			)
			NavigationUtil.goToArtistOptions(this, album.artistId, artistPairs)
		}
		playAction.apply {
			setOnClickListener { MusicPlayerRemote.openQueue(album.songs!!, 0, true) }
		}
		shuffleAction.apply {
			setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(album.songs!!, true) }
		}
	}

	private fun setupRecyclerView() {
		simpleSongAdapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song, this)
		recyclerView.apply {
			layoutManager = LinearLayoutManager(this@AlbumDetailsActivity)
			itemAnimator = DefaultItemAnimator()
			isNestedScrollingEnabled = false
			adapter = simpleSongAdapter
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		albumDetailsPresenter.detachView()
	}

	override fun complete() {
		ActivityCompat.startPostponedEnterTransition(this)
	}

	override fun album(album: Album) {
		complete()
		if (album.songs!!.isEmpty()) {
			finish()
			return
		}
		this.album = album

		albumTitle.text = album.title