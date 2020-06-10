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
