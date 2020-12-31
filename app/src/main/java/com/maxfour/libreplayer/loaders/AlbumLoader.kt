package com.maxfour.libreplayer.loaders

import android.content.Context
import android.provider.MediaStore.Audio.AudioColumns
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.PreferenceUtil
import java.util.*
import kotlin.collections.ArrayList

object AlbumLoader {

    fun getAlbums(
            context: Context,
            query: