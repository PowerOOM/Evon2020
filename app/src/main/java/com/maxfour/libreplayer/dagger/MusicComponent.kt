package com.maxfour.libreplayer.dagger

import com.maxfour.libreplayer.activities.*
import com.maxfour.libreplayer.dagger.module.*
import com.maxfour.libreplayer.fragments.mainactivity.*
import com.maxfour.libreplayer.fragments.mainactivity.home.BannerHomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            PresenterModule::class
        ]
)
interface MusicComponent {

    fun