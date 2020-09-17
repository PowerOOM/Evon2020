package com.maxfour.libreplayer.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.artist.ArtistAdapter
import com.maxfour.libreplayer.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import com.maxfour.libreplayer.model.Artist
import com.maxfour.libreplayer.mvp.presenter.ArtistsPresenter
import com.maxfour.libreplayer.mvp.presenter.ArtistsView
import com.maxfour.libreplayer.util.PreferenceUtil
import javax.inject.Inject

class ArtistsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
        ArtistsView {

    override fun artists(artists: ArrayList<Artist>) {
        adapter?.swapDataSet(artists)
    }

    @Inject
    lateinit var artistsPresenter: ArtistsPresenter

    override val emptyMessage: Int
        get() = R.string.no_artists

    override