package com.maxfour.libreplayer.extensions

import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.model.Song

fun ArrayList<Song>.lastElement(): Boolean {
    println("${this.size} ${this.indexOf(MusicPlayerRemote.currentSong)}")
    r