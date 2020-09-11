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
       