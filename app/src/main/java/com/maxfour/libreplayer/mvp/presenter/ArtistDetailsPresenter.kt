package com.maxfour.libreplayer.mvp.presenter

import com.maxfour.libreplayer.Result
import com.maxfour.libreplayer.model.Artist
import com.maxfour.libreplayer.mvp.BaseView
import com.maxfour.libreplayer.mvp.Presenter
import com.maxfour.libreplayer.mvp.PresenterImpl
import com.maxfour.libreplayer.providers.interfaces.Repository
import com.maxfour.libreplayer.rest.model.LastFmArtist
import kotlinx.coroutines.*
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interfa