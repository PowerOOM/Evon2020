
package com.maxfour.libreplayer.helper

import com.maxfour.libreplayer.model.Song

object ShuffleHelper {

    fun makeShuffleList(listToShuffle: MutableList<Song>, current: Int) {
        if (listToShuffle.isEmpty()) return
        if (current >= 0) {
            val song = listToShuffle.removeAt(current)
            listToShuffle.shuffle()
            listToShuffle.add(0, song)
        } else {
            listToShuffle.shuffle()
        }
    }
}