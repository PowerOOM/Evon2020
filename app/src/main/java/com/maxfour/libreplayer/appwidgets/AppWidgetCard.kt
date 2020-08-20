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
				PlayerUtil.getTintedVectorDrawable(
						context,
						R.drawable.ic_skip_previous_white_24dp,
						MaterialValueHelper.getSecondaryTextColor(
								context, true
						)
				)!!, 1f
		)
		)
		appWidgetView.setImageViewBitmap(
				R.id.button_toggle_play_pause, createBitmap(
				PlayerUtil.getTintedVectorDrawable(
						context,
						R.drawable.ic_play_arrow_white_32dp,
						MaterialValueHelper.getSecondaryTextColor(
								context, true
						)
				)!!, 1f
		)
		)

		linkButtons(context, appWidgetView)
		pushUpdate(context, appWidgetIds, appWidgetView)
	}

	/**
	 * Update all active widget instances by pushing changes
	 */
	override fun performUpdate(service: MusicService, appWidgetIds: IntArray?) {
		val appWidgetView = RemoteViews(service.packageName, R.layout.app_widget_card)

		val isPlaying = service.isPlaying
		val song = service.currentSong

		// Set the titles and artwork
		if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
			appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE)
		} else {
			appWidgetView.setViewVisibility(R.id.media_titles, View.VISIBLE)
			appWidgetView.setTextViewText(R.id.title, song.title)
			appWidgetView.setTextViewText(R.id.text, getSongArtistAndAlbum(song))
		}

		// Set correct drawable for pause state
		val playPauseRes = if (isPlaying) R.drawable.ic_pause_white_24dp else R.drawable.ic_play_arrow_white_32dp
		appWidgetView.setImageViewBitmap(
				R.id.button_toggle_play_pause, createBitmap(
				PlayerUtil.getTintedVectorDrawable(
						service,
						playPauseRes,
						MaterialValueHelper.getSecondaryTextColor(service, true)
				)!!, 1f
		)
		)

		// Set prev/next button drawables
		appWidgetView.setImageViewBitmap(
				R.id.button_next, createBitmap(
				PlayerUtil.getTintedVectorDrawable(
						service,
						R.drawable.ic_skip_next_white_24dp,
						MaterialValueHelper.getSecondaryTextColor(service, true)
				)!!, 1f
		)
		)
		appWidgetView.setImageViewBitmap(
				R.id.button_prev, createBitmap(
				PlayerUtil.getTintedVectorDrawable(
						service,
						R.drawable.ic_skip_previous_white_24dp,
						MaterialValueHelper.getSecondaryTextColor(service, true)
				)!!, 1f
		)
		)

		// Link actions buttons to intents
		linkButtons(service, appWidgetView)

		if (imageSize == 0) {
			imageSize = service.resources.getDimensionPixelSize(com.maxfour.libreplayer.R.dimen.app_widget_card_image_size)
		}
		if (cardRadius == 0f) {
			cardRadius = service.resources.getDimension(com.maxfour.libreplayer.R.dimen.app_widget_card_radius)
		}
		val appContext = service.applicationContext
		// Load the album cover async and push the update on completion
		service.runOnUiThread {
			if (target != null) {
				Glide.clear(target)
			}
			target = SongGlideRequest.Builder.from(Glide.with(service), song)
				.checkIgnoreMediaStore(service).generatePalette(service).build().centerCrop()
				.into(object : SimpleTarget<BitmapPaletteWrapper>(imageSize, imageSize) {
					override fun onResourceReady(
							resource: BitmapPaletteWrapper,
							glideAnimation: GlideAnimation<in BitmapPaletteWrapper>
					) {
						val palette = resource.palette
						update(
								resource.bitmap, palette.getVibrantColor(
								palette.getMutedColor(
										MaterialValueHelper.getSecondaryTextColor(
												service, true
										)
								)
						)
						)
					}

					override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
						super.onLoadFailed(e, errorDrawable)
						update(null, MaterialValueHelper.getSecondaryTextColor(service, true))
					}

					private fun update(bitmap: Bitmap?, color: Int) {
						// Set correct drawable for pause state
						appWidgetView.setImageViewBitmap(
								R.id.button