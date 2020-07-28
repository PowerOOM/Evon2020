package com.maxfour.libreplayer.appshortcuts

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.util.PlayerUtil
import com.maxfour.libreplayer.util.PreferenceUtil

@RequiresApi(Build.VERSION_CODES.N_MR1)
object AppShortcutIconGenerator {
	fun generateThemedIcon(context: Context, iconId: Int): Icon {
		return if (PreferenceUtil.getInstance(context).coloredAppShortcuts()) {
			generateUserThemedIcon(context, iconId)
		} else {
			generateDefaultThemedIcon(context, iconId)
		}
	}

	private fun generateDefaultThemedIcon(context: Context, iconId: Int): Icon {
		// Return an Icon of iconId with default colors
		return generateThemedIcon(
				context,
				iconId,
				context.getColor(R.color.app_shortcut_default_foreground),
				context.getColor(R.color.app_shortcut_default_background)
		)
	}

	private fun generateUserThemedIcon(context: Context, iconId: Int): Icon {
		// Get background color from context's theme
		val typedColorBackground = TypedValue()
		context.theme.resolveAttribute(android.R.attr.colorBackground, typedColorBackground, true)

		// Return an Icon of ico