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

class MediaSessionCallback(private val context: Contex