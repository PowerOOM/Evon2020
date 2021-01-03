package com.maxfour.libreplayer.loaders

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.MediaStore.Audio.PlaylistsColumns
import com.maxfour.libreplayer.model.Playlist
import io.reactivex.Observable
import java.util.*

object PlaylistLoader {

    private fun getPlaylistFlowable(
            cursor: Cursor?
    ): Observable<Playlist> {
        return Observable.create { e ->
            var playlist = Playlist()

            if (cursor != null && cursor.moveToFirst()) {
                playlist = getPlaylistFromCursorImpl(cursor)
            }
            cursor?.close()

            e.onNext(playlist)
            e.onComplete()
        }
    }

    fun getPlaylist(
            cursor: Cursor?
    ): Playlist {
        var playlist = Playlist()

        if (cursor != null && cursor.moveToFirst()) {
            playlist = getPlaylistFromCursorImpl(cursor)
        }
        cursor?.close()
        return playlist
    }

    fun getPlaylistFlowable(
            context: Context,
            playlistName: String
    ): Observable<Playlist> {
        return getPlaylistFlowable(makePlaylistCursor(
                context,
                PlaylistsColumns