package com.maxfour.libreplayer.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.MusicPlayerRemote.cycleRepeatMode
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.service.MusicService.*
import com.maxfour.libreplayer.util.MusicUtil
import java.util.*

class MediaSessionCallback(private val context: Context,
                           private val musicService: MusicService
) : MediaSessionCompat.Callback() {

    override fun onPlay() {
        super.onPlay()
        musicService.play()
    }

    override fun onPause() {
        super.onPause()
        musicService.pause()
    }

    override fun onSkipToNext() {
        super.onSkipToNext()
        musicService.playNextSong(true)
    }

    override fun onSkipToPrevious() {
        super.onSkipToPrevious()
        musicService.back(true)
    }

    override fun onStop() {
        super.onStop()
        musicService.quit()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        musicService.seek(pos.toInt())
    }

    override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
        return MediaButtonIntentReceiver.handleIntent(context, mediaButtonIntent)
    }

    override fun onCustomAction(action: String, extras: Bundle?) {
        when (action) {
            CYCLE_REPEAT -> {
                cycleRepeatMode()
                musicService.updateMediaSessionPlaybackState()
            }

            TOGGLE_SHUFFLE -> {
                musicService.toggleShuffle()
                musicService.updateMediaSessionPla