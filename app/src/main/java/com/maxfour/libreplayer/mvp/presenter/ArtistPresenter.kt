package com.maxfour.libreplayer.mvp.presenter

import com.maxfour.libreplayer.Result
import com.maxfour.libreplayer.model.Artist
import com.maxfour.libreplayer.mvp.BaseView
import com.maxfour.libreplayer.mvp.Presenter
import com.maxfour.libreplayer.mvp.PresenterImpl
import com.maxfour.libreplayer.providers.interfaces.Repository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface ArtistsView : BaseView {
    fun artists(artists: ArrayList<Artist>)
}

interface ArtistsPresenter : Presenter<ArtistsView> {

    fun loadArtists()

    class ArtistsPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<ArtistsView