package com.maxfour.libreplayer.mvp.presenter

import com.maxfour.libreplayer.Result.Error
import com.maxfour.libreplayer.Result.Success
import com.maxfour.libreplayer.mvp.BaseView
import com.maxfour.libreplayer.mvp.Presenter
import com.maxfour.libreplayer.mvp.PresenterImpl
import com.maxfour.libreplayer.providers.interfaces.Repository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface SearchView : BaseView {
    fun showData(data: MutableList<Any>)
}

interface SearchPresenter : Presenter<SearchView> {

    fun search(query: String?)

    class SearchPresenterImpl @Inject constructor(
            private val