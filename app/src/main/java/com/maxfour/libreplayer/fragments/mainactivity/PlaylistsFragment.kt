package com.maxfour.libreplayer.fragments.mainactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.playlist.PlaylistAdapter
import com.maxfour.libreplayer.fragments.base.AbsLibraryPagerRecyclerViewFragment
import com.maxfour.libreplayer.model.Playlist
import com.maxfour.libreplayer.mvp.presenter.PlaylistView
import com.maxfour.libreplayer.mvp.presenter.PlaylistsPresenter
import javax.inject.Inject


class PlaylistsFragment : AbsLibraryPagerRecyclerViewFragment<PlaylistAdapter, LinearLayoutManager>(), PlaylistView {

    @Inject
    lateinit var playlistsPresenter: PlaylistsPresenter

    override val emptyMessage: Int
        get() = R.string.no_playlists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, s