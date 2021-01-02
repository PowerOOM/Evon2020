package com.maxfour.libreplayer.loaders

import android.content.Context
import android.provider.MediaStore.Audio.AudioColumns
import com.maxfour.libreplayer.model.Album
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.PreferenceUtil
import java.util.*
import kotlin.collections.ArrayList

object AlbumLoader {

    fun getAlbums(
            context: Context,
            query: String
    ): ArrayList<Album> {
        val songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                AudioColumns.ALBUM + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder(context))
        )
        return splitIntoAlbums(songs)
    }

    fun getAlbum(
            context: Context,
            albumId: Int
    ): Album {
        val songs = SongLoader.getSongs(
                SongLoader.makeSongCursor(
                        context,
                        AudioColumns.ALBUM_ID + "=?",
                        arrayOf(albumId.toString()),
                        getSongLoaderSortOrder(context)))
        val album = Album(songs)
        sortSongsByTrackNumber(album)
        return album
    }

    fun getAllAlbums(
            context: Context
    ): ArrayList<Album> {
        val songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, null, null, getSongLoaderSortOrder(context)))
        return splitIntoAlbums(songs)
    }

    fun splitIntoAlbums(
            songs: ArrayList<Song>?
    ): ArrayList<Album> {
        val albums = ArrayList<Album>()
        if (songs != null) {
            for (song in songs) {
                getOrCreateAlbum(albums, song