package com.maxfour.libreplayer.activities.base

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.maxfour.appthemehelper.ATH
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.common.ATHToolbarActivity
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.TintHelper
import com.maxfour.appthemehelper.util.VersionUtils
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.util.PlayerUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.ThemeManager

abstract class AbsThemeActivity : ATHToolbarActivity(), Runnable {

	private val handler = Handler()

	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(ThemeManager.getThemeResValue(this))
		hideStatusBar()
		super.onCreate(savedInstanceState)
		setImmersiveFullscreen()
		registerSystemUiVisibility()
		toggleScreenOn()
	}

	private fun toggleScreenOn() {
		if (PreferenceUtil.getInstance(this).isScreenOnEnabled) {
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		} else {
			window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		super.onWindowFocusChanged(hasFocus)
		if (hasFocus) {
			hideStatusBar()
			handler.removeCallbacks(this)
			handler.postDelayed(this, 300)
		} else {
			handler.removeCallbacks(this)
		}
	}

	fun hideStatusBar() {
		hideStatusBar(PreferenceUtil.getInstance(this).fullScreenMode)
	}

	private fun hideStatusBar(fullscreen: Boolean) {
		val statusBar = window.decorView.rootView.findViewById<View>(R.id.status_bar)
		if (statusBar != null) {
			statusBar.visibility = if (fullscreen) View.GONE else View.VISIBLE
		}
	}

	private fun changeBackgroundShape() {
		var background: Drawable? = if (PreferenceUtil.getInstance(this).isRoundCorners)
			ContextCompat.getDrawable(this, R.drawable.round_window)
		else ContextCompat.getDrawable(this, R.drawable.square_window)
		background =
				TintHelper.createTintedDrawable(background, ATHUtil.resolveColor(this, android.R.attr.windowBackground))
		window.setBackgroundDrawable(background)
	}

	fun setDrawUnderStatusBar() {
		PlayerUtil.setAllowDrawUnderStatusBar(window)
	}

	fun setDrawUnderNavigationBar() {
		PlayerUtil.