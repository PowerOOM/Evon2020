package com.maxfour.libreplayer.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.MainActivity
import com.maxfour.libreplayer.appwidgets.base.BaseAppWidget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.glide.palette.BitmapPaletteWrapper
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.service.MusicService.*
import com.maxfour.libreplayer.util.ImageUtil
import com.maxfour.libreplayer.util.PlayerUtil

class AppWidgetCard : BaseAppWidget() {
	private var target: Target<BitmapPaletteWrapper>? = null // for cancellation

	/**
	 * Initialize given widgets to default state, where we launch Music on default click and hide
	 * actions if service not running.
	 */
	override fun defaultAppWidget(context: Context, appWidgetIds: IntArray) {
		val appWidgetView = RemoteViews(context.packageName, R.layout.app_widget_card)

		appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE)
		appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art)
		appWidgetView.setImageViewBitmap(
				R.id.button_next, createBitmap(
				PlayerUtil.getTintedVectorDrawable(
						context,
						R.drawable.ic_skip_next_white_24dp,
						MaterialValueHelper.getSecondaryTextColor(
								context, true
						)
				)!!, 1f
		)
		)
		appWidgetView.setImageViewBitmap(
				R.id.button_prev, createBitmap(
				PlayerUtil.getTintedVector