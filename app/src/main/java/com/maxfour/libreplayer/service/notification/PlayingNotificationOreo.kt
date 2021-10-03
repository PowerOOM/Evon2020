package com.maxfour.libreplayer.service.notification

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.MainActivity
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.glide.palette.BitmapPaletteWrapper
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.service.MusicService.*
import com.maxfour.libreplayer.util.PlayerUtil
import com.maxfour.libreplayer.util.PlayerUtil.createBitmap
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.color.MediaNotificationProcessor

class PlayingNotificationOreo : PlayingNotification() {

    private var target: Target<BitmapPaletteWrapper>? = null

    private fun getCombinedRemoteViews(collapsed: Boolean, song: Song): RemoteViews {
        val remoteViews = RemoteViews(service.packageName, if (collapsed) R.layout.layout_notification_collapsed else R.layout.layout_notification_expanded)
        remoteViews.setTextViewText(R.id.appName, service.getString(R.string.app_name) + " • " + song.albumName)
        remoteViews.setTextViewText(R.id.title, song.title)
        remoteViews.setTextViewText(R.id.subtitle, song.artistName)
        linkButtons(remoteViews)
        return remoteViews
    }

    override fun update() {
        stopped = false
        val song = service.currentSong
        val isPlaying = service.isPlaying

        val notificationLayout = getCombinedRemoteViews(true, song)
        val notificationLayoutBig = getCombinedRemoteViews(false, song)

        val action = Intent(service, MainActivity::class.java)
        action.putExtra("expand", true)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val clickIntent = PendingIntent
                .getActivity(service, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
        val deleteIntent = buildPendingIntent(service, ACTION_QUIT, null)

        val builder = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(clickIntent)
                .setDeleteIntent(deleteIntent)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutBig)
                .setOngoing(isPlaying)

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
                            val mediaNotificat