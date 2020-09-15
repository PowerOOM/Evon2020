package com.maxfour.libreplayer.fragments.mainactivity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.album.AlbumAdapter
import com.maxfour.libreplayer.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.mvp.presenter.AlbumsPresenter
import com.maxfour.libreplayer.mvp.presenter.AlbumsView
import com.maxfour.libreplayer.util.PreferenceUtil
import javax.inject.Inject

open class AlbumsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager>(),
        AlbumsView {

    @Inject
    lateinit var albumsPresenter: AlbumsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        if (adapter!!.dataSet.isEmpty()) {
            albumsPresenter.loadAlbums()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        albumsPresenter.detachView()
    }

    override fun albums(albums: java.util.ArrayList<Album>) {
        adapter?.swapDataSet(albums)
    }

    override val emptyMessage: Int
        get() = R.string.no_albums

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, getGridSize())
    }

    override fun createAdapter(): AlbumAdapter {
        var itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance(requireContext()).getAlbumGridStyle(requireContext())
        }
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return AlbumAdapter(libraryFragment.mainActivity, dataSet, itemLayoutRes, loadUsePalette(), libraryFragment)
    }

    public override fun loadUsePalette(): Boolean {
        return PreferenceUtil.getInstance(requireContext()).albumColoredFooters()
    }

    override fun setUsePalette(usePalette: Boolean) {
        adapter?.usePalette(usePalette)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {

        return PreferenceUtil.getInstance(requireContext()).albumSortOr