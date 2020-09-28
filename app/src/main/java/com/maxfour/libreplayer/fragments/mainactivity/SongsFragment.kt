package com.maxfour.libreplayer.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.song.ShuffleButtonSongAdapter
import com.maxfour.libreplayer.adapter.song.SongAdapter
import com.maxfour.libreplayer.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.mvp.presenter.SongPresenter
import com.maxfour.libreplayer.mvp.presenter.SongView
import com.maxfour.libreplayer.util.PreferenceUtil
import java.util.*
import javax.inject.Inject

class SongsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>(), SongView {

    @Inject
    lateinit var songPresenter: SongPresenter


    override val empty