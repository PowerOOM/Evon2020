package com.maxfour.libreplayer.service.notification

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.MainActivity
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.glide.palette.BitmapPaletteWrapper
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.service.MusicService.*
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.PlayerColorUtil
import com.maxfour.libreplayer.util.PreferenceUtil

class PlayingNotificationImpl : PlayingNotification() {
    private var target: Target<BitmapPaletteWrapper>? = null
    @Synchronized
    override fun update() {
        stopped = false

        val song = service.currentSong
        val isPlaying = service.isPlaying
        val isFavorite = MusicUtil.isFavorite(service, song)
        val playButtonResId = if (isPlaying) R.drawable.ic_pause_white_48dp else R.drawable.ic_play_arrow_white_48dp
        val favoriteResId = if (isFavorite) R.drawable.ic_favorite_white_24dp else R.drawable.ic_favorite_border_white_24dp

        val action = Intent(service, MainActivity::class.java)
        action.putExtra("expand", true)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val clickIntent = PendingIntent.getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)

        val serviceName = ComponentName(service, MusicService::class.java)
        val intent = Intent(ACTION_QUIT)
        intent.component = serviceName
        val deleteIntent = PendingIntent.getService(service, 0, intent, 0)

        val bigNotificationImageSize = service.resources
                .getDimensionPixelSize(R.dimen.notification_big_image_size)
        service.runOnUiThread {
            if (target != null) {
                Glide.clear(target)
            }
            target = SongGlideRequest.Builder.from(Glide.with(service), song)
                    .checkIgnoreMediaStore(service)
                    .generatePalette(service).build()
                    .centerCrop()
                    .into(object : SimpleTarget<BitmapPaletteWrapper>(bigNotificationImageSize, bigNotificationImageSize) {
                        override fun onResourceReady(resource: BitmapPaletteWrapper, glideAnimation: GlideAnimation<in BitmapPaletteWrapper>) {
                            update(resource.bitmap, when {
                                PreferenceUtil.getInstance(service).isDominantColor -> PlayerColorUtil.getDominantColor(resource.bitmap, Color.TRANSPARENT)
                                else -> PlayerColorUtil.getColor(resource.palette, Color.TRANSPARENT)
                            })
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            update(null, Color.TRANSPARENT)
                        }

                        fun update(bitmap: Bitmap?, color: Int) {
                         