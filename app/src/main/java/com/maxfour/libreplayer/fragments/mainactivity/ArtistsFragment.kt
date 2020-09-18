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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            artistsPresenter.loadArtists()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        artistsPresenter.detachView()
    }

    override fun onMediaStoreChanged() {
        artistsPresenter.loadArtists()
    }

    override fun setSortOrder(sortOrder: String) {
        artistsPresenter.loadArtists()
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): ArtistAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance(requireContext()).getArtistGridStyle(requireContext())
        }
        val dataSet 