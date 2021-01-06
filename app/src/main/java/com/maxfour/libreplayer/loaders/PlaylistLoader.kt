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
                PlaylistsColumns.NAME + "=?",
                arrayOf(playlistName)
        ))
    }

    fun getPlaylist(
            context: Context,
            playlistName: String
    ): Playlist {
        return getPlaylist(makePlaylistCursor(
                context,
                PlaylistsColumns.NAME + "=?",
                arrayOf(playlistName)
        ))
    }

    fun getPlaylistFlowable(
            context: Context,
            playlistId: Int
    ): Observable<Playlist> {
        return getPlaylistFlowable(makePlaylistCursor(
                context,
                BaseColumns._ID + "=?",
                arrayOf(playlistId.toString())
        ))
    }

    fun getAllPlaylistsFlowoable(
            context: Context
    ): Observable<ArrayList<Playlist>> {
        return getAllPlaylistsFlowable(makePlaylistCursor(context, null, null))
    }

    fun getFavoritePlaylistFlowable(context: Context): Observable<ArrayList<Playlist>> {
        return getAllPlaylistsFlowable(makePlaylistCursor(
                context,
                PlaylistsColumns.NAME + "=?",
                arrayOf(context.getString(com.maxfour.libreplayer.R.string.favorites))))
    }

    private fun getAllPlaylistsFlowable(cursor: Cursor