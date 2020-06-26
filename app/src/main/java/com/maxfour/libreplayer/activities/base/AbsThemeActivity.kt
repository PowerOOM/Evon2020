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
			window.addFlags(WindowManager.LayoutPara