package com.maxfour.libreplayer.adapter.album

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.libreplayer.glide.PlayerColoredTarget
import com.maxfour.libreplayer.glide.SongGlideRequest
import com.maxfour.libreplayer.helper.HorizontalAdapterHelper
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.util.MusicUtil
import java.util.*

class HorizontalAlbumAdapter