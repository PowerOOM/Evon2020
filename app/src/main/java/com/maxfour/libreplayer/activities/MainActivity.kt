package com.maxfour.libreplayer.activities

import android.content.*
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.base.AbsSlidingMusicPanelActivity
import com.maxfour.libreplayer.fragments.mainactivity.LibraryFragment
import com.maxfour.libreplayer.fragments.mainactivity.folders.FoldersFragment
import com.maxfour.libreplayer.fragments.mainactivity.home.BannerHomeFragment
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.SearchQueryHelper
import com.maxfour.libreplayer.interfaces.MainActivityFragmentCallbacks
import com.maxfour.libreplayer.loaders.AlbumLoader
import com.maxfour.libreplayer.loaders.ArtistLoader
import com.maxfour.libreplayer.loaders.PlaylistSongsLoader
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.util.PreferenceUtil

import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

class MainActivity : AbsSlidingMusicPanelActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

	private lateinit var currentFragment: MainActivityFragmentCallbacks

	private var blockRequestPermissions: Boolean = false
	private val disposable = CompositeDisposable()
	private val broadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			val action = intent.action
			if (action != null && action == Intent.ACTION_SCREEN_OFF) {
				if (PreferenceUtil.getInstance(this@MainActivity).lockScreen && MusicPlayerRemote.isPlaying) {
					val activity = Intent(context, LockScreenActivity::class.java)
					activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
					activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
					ActivityCompat.startActivity(context, activity, null)
				}
			}
		}
	}

	override fun createContentView(): View {
		return wrapSlidingMusicPanel(R.layout.activity_main_content)
	}

	override fun onCreate(
		savedInstanceState: Bundle?
	) {
		setDrawUnderStatusBar()
		super.onCreate(savedInstanceState)
		getBottomNavigationView().selectedItemId = PreferenceUtil.getInstance(this).lastPage
		getBottomNavigationView().setOnNavigationItemSelectedListener {
			PreferenceUtil.getInstance(this).lastPage = it.itemId
			selectedFragment(it.itemId)
			true
		}

		if (savedInstanceState == null) {
			setMusicChooser(PreferenceUtil.getInstance(this).lastMusicChooser)
		} else {
			restoreCurrentFragment()
		}
	}

	override fun onResume() {
		super.onResume()
		val screenOnOff = IntentFilter()
		screenOnOff.addAction(Intent.ACTION_SCREEN_OFF)
		registerReceiver(broadcastReceiver, screenOnOff)

		PreferenceUtil.getInstance(this).registerOnSharedPreferenceChangedListener(this)

		if (intent.hasExtra("expand")) {
			if (intent.getBooleanExtra("expand", false)) {
				expandPanel()
				intent.putExtra("expand", false)
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		disposable.clear()
		unregisterReceiver(broadcastReceiver)
		PreferenceUtil.getInstance(this).unregisterOnSharedPreferenceChangedListener(this)
	}

	private fun setCurrentFragment(fragment: Fragment, tag: String) {
		if (tag != supportFragmentManager.findFragmentById(R.id.fragment_container)?.tag) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragment_container, fragment, tag).commit()
			currentFragment = fragment as MainActivityFragmentCallbacks
		}
	}

	private fun restoreCurrentFragment() {
		currentFragment =
				supportFragmentManager.findFragmentById(R.id.fragment_container) as MainActivityFragmentCallbacks
	}

	private fun handlePlaybackIntent(intent: Intent?) {
		if (intent == null) {
			return
		}
		val uri = intent.data
		val mimeType = intent.type
		var handled = false
		if (intent.action != null && intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH) {
			val songs = SearchQueryHelper.getSongs(this, intent.extras!!)
			if (MusicPlayerRemote.shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) {
				MusicPlayerRemote.openAndShuffleQueue(songs, true)
			} else {
				MusicPlayerRemote.openQueue(songs, 0, true)
			}
			handled = true
		}

		if (uri != null && uri.toString().isNotEmpty()) {
			MusicPlayerRemote.playFromUri(uri)
			handled = true
		} else if (MediaStore.Audio.Playlists.CONTENT_TYPE == mimeType) {
			val id = parseIdFromIntent(intent, "playlistId", "playlist").toInt()
			if (id >= 0) {
				val position = intent.getIntExtra("position", 0)
				val songs = ArrayList(PlaylistSongsLoader.getPlaylistSongList(this, id))
				MusicPlayerRemote.openQueue(songs, position, true)
				handled = true
			}
		} else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
			val id = parseIdFromIntent(intent, "albumId", "album").toInt()
			if (id >= 0) {
				val position = intent.getIntExtra("position", 0)
				MusicPlayerRemote.openQueue(AlbumLoader.getAlbum(this, id).songs!!, position, true)
				handled = true
			}
		} else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
			val id = parseIdFromIntent(intent, "artistId", "artist").toInt()
			if (id >= 0) {
				val position = intent.getIntExtra("position", 0)
				MusicPlayerRemote.openQueue(ArtistLoader.getArtist(this, id).songs, position, true)
				handled = true
			}
		}
		if (handled) {
			setIntent(Intent())
		}
	}

	private fun parseIdFromIntent(intent: Intent, longKey: String, stringKey: String): Long {
		var id = intent.getLongExtra(longKey, -1)
		if (id < 0) {
			val idString = intent.getStringExtra(stringKey)
			if (idString != null) {
				try {
					id = java.lang.Long.parseLong(idString)
				} catch (e: NumberFormatException) {
					Log.e(TAG, e.message)
				}
			}
		}
		return id
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			APP_INTRO_REQUEST -> {
				blockRequestPermissions = false
				if (!hasPermissions()) {
					requestPermissions()
				}
			}
			REQUEST_CODE_THEME, APP_USER_INFO_REQUEST -> postRecreate()
			PURCHASE_REQUEST -> {
				if (resultCode == RESULT_OK) {
					//checkSetUpPro();
				}
			}
		}
	}

	override fun handleBackPress(): Boolean {
		return super.handleBackPress() || currentFragment.handleBackPress()
	}

	override fun onServiceConnected() {
		super.onServiceConnected()
		handlePlaybackIntent(intent)
	}

	override fun requ