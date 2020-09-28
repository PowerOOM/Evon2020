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


    override val emptyMessage: Int
        get() = R.string.no_songs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songPresenter.attachView(this)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): SongAdapter {
        val itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        val usePalette = loadUsePalette()

        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet

        return if (getGridSize() <= maxGridSizeForList) {
            ShuffleButtonSongAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, usePalette, libraryFragment)
        } else SongAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, usePalette, libraryFragment)
    }

    override fun songs(songs: ArrayList<Song>) {
        adapter?.swapDataSet(songs)
    }

    override fun onMediaStoreChanged() {
        songPresenter.loadSongs()
    }

    override