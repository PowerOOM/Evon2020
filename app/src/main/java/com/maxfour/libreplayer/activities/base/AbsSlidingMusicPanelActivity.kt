package com.maxfour.libreplayer.activities.base

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.extensions.hide
import com.maxfour.libreplayer.extensions.show
import com.maxfour.libreplayer.fragments.MiniPlayerFragment
import com.maxfour.libreplayer.fragments.NowPlayingScreen
import com.maxfour.libreplayer.fragments.NowPlayingScreen.*
import com.maxfour.libreplayer.fragments.base.AbsPlayerFragment
import com.maxfour.libreplayer.fragments.player.adaptive.AdaptiveFragment
import com.maxfour.libreplayer.fragments.player.blur.BlurPlayerFragment
import com.maxfour.libreplayer.fragments.player.card.CardFragment
import com.maxfour.libreplayer.fragments.player.cardblur.CardBlurFragment
import com.maxfour.libreplayer.fragments.player.color.ColorFragment
import com.maxfour.libreplayer.fragments.player.fit.FitFragment
import com.maxfour.libreplayer.fragments.player.flat.FlatPlayerFragment
import com.maxfour.libreplayer.fragments.player.full.FullPlayerFragment
import com.maxfour.libreplayer.fragments.player.material.MaterialFragment
import com.maxfour.libreplayer.fragments.player.normal.PlayerFragment
import com.maxfour.libreplayer.fragments.player.peak.PeakPlayerFragment
import com.maxfour.libreplayer.fragments.player.plain.PlainPlayerFragment
import com.maxfour.libreplayer.fragments.player.simple.SimplePlayerFragment
import com.maxfour.libreplayer.fragments.player.tiny.TinyPlayerFragment
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.model.CategoryInfo
import com.maxfour.libreplayer.util.DensityUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.views.BottomNavigationBarTinted
import kotlinx.android.synthetic.main.sliding_music_panel_layout.*

abstract class AbsSlidingMusicPanelActivity : AbsMusicServiceActivity(), AbsPlayerFragment.Callbacks {
	companion object {
		val TAG: String = AbsSlidingMusicPanelActivity::class.java.simpleName
	}

	private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
	private var miniPlayerFragment: MiniPlayerFragment? = null
	private var playerFragment: AbsPlayerFragment? = null
	private var currentNowPlayingScreen: NowPlayingScreen? = null
	private var navigationBarColor: Int = 0
	private var taskColor: Int = 0
	private var lightStatusBar: Boolean = false
	private var lightNavigationBar: Boolean = false
	private var navigationBarColorAnimator: ValueAnimator? = null
	protected abstract fun createContentView(): View
	private val panelState: Int
		get() = bottomSheetBehavior.state

	private val bottomSheetCallbackList = object : BottomSheetBehavior.BottomSheetCallback() {

		override fun onSlide(bottomSheet: View, slideOffset: Float) {
			setMiniPlayerAlphaProgress(slideOffset)
			dimBackground.show()
			dimBackground.alpha = slideOffset
		}

		override fun onStateChanged(bottomSheet: View, newState: Int) {
			when (newState) {
				BottomSheetBehavior.STATE_EXPANDED -> {
					onPanelExpanded()
				}
				BottomSheetBehavior.STATE_COLLAPSED -> {
					onPanelCollapsed()
					dimBackground.hide()
				}
				else -> {

				}
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(createContentView())

		chooseFragmentForTheme()
		setupSlidingUpPanel()

		updateTabs()

		bottomSheetBehavior = BottomSheetBehavior.from(slidingPanel)

		val themeColor = ATHUtil.resolveColor(this, android.R.attr.windowBackground, Color.GRAY)
		dimBackground.setBackgroundColor(ColorUtil.withAlpha(themeColor, 0.5f))
	}

	override fun onResume() {
		super.onResume()
		if (currentNowPlayingScreen != PreferenceUtil.getInstance(this).nowPlayingScreen) {
			postRecreate()
		}
		bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallbackList)

		if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
			setMiniPlayerAlphaProgress(1f)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallbackList)
		if (navigationBarColorAnimator != null) navigationBarColorAnimator?.cancel() // just in case
	}

	protected fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
		val slidingMusicPanelLayout = layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
		val contentContainer = slidingMusicPanelLayout.findViewById<ViewGroup>(R.id.mainContentFrame)
		layoutInflater.inflate(resId, contentContainer)
		return slidingMusicPanelLayout
	}

	private fun collapsePanel() {
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
	}

	fun expandPanel() {
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
		setMiniPlayerAlphaProgress(1f)
	}

	private fun setMiniPlayerAlphaProgress(progress: Float) {
		if (miniPlayerFragment?.view == null) return
		val alpha = 1 - progress
		miniPlayerFragment?.view?.alpha = alpha
		// necessary to make the views below clickable
		miniPlayerFragment?.view?.visibility = if (alpha == 0f) View.GONE else View.VISIBLE

		bottomNavigationView.translationY = progress * 500
		//bottomNavigationView.alpha = alpha
	}

	open fun onPanelCollapsed() {
		// restore values
		super.setLightStatusbar(lightStatusBar)
		super.setTaskDescriptionColor(taskColor)
		super.setNavigationbarColor(navigationBarColor)
		super.setLightNavigationBar(lightNavigationBar)


		playerFragment?.setMenuVisibility(false)
		playerFragment?.userVisibleHint = false
		playerFragment?.onHide()
	}

	open fun onPanelExpanded() {
		val playerFragmentColor = playerFragment!!.paletteColor
		super.setTaskDescriptionColor(playerFragmentColor)

		playerFragment?.setMenuVisibility(true)
		playerFragment?.userVisibleHint = true
		playerFragment?.onShow()
		onPaletteColorChanged()
	}

	private fun setupSlidingUpPanel() {
		slidingPanel.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				slidingPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
				if (currentNowPlayingScreen != PEAK) {
					val params = slidingPanel.layoutParams as ViewGroup.LayoutParams
					params.height = ViewGroup.LayoutParams.MATCH_PARENT
					slidingPanel.layoutParams = params
				}
				when (panelState) {
					BottomSheetBehavior.STATE_EXPANDED -> onPanelExpanded()
					BottomSheetBehavior.STATE_COLLAPSED -> onPanelCollapsed()
					else -> playerFragment!!.onHide()
				}
			}
		})
	}

	fun toggleBottomNavigationView(toggle: Boolean) {
		bottomNavigationView.visibility = if (toggle) View.GONE else View.VISIBLE
	}

	fun getBottomNavigationView(): BottomNavigationBarTinted {
		return bottomNavigationView
	}

	private fun hideBottomBar(hide: Boolean) {
		val heightOfBar = resources.getDimensionPixelSize(R.dimen.mini_player_height)
		val heightOfBarWithTabs = resources.getDimensionPixelSize(R.dimen.mini_player_height_expanded)

		if (hide) {
			bottomSheetBehavior.isHideable = true
			bottomSheetBehavior.peekHeight = 0
			collapsePanel()
			bottomNavigationView.elevation = DensityUtil.dip2px(this, 10f).toFloat()
		} else {
			if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
				slidingPanel.cardElevation = DensityUtil.dip2px(this, 10f).toFloat()
				bottomNavigationView.elevation = DensityUtil.dip2px(this, 10f).toFloat()
				bottomSheetBehavior.isHideable = false
				bottomSheetBehavior.peekHeight =
						if (bottomNavigationView.visibility == View.VISIBLE) heightOfBarWithTabs else heightOfBar
			}
		}
	}

	fun setBottomBarVisibility(gone: Int) {
		bottomNavigationView.visibility = gone
		hideBottomBar(false)
	}

	private fun chooseFragmentForTheme() {
		currentNowPlayingScreen = PreferenceUtil.getInstance(this).nowPlayingScreen

		val fragment: Fragment = w