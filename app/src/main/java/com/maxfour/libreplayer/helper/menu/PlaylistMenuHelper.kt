package com.maxfour.libreplayer.helper.menu


import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxfour.libreplayer.App
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.dialogs.AddToPlaylistDialog
import com.maxfour.libreplayer.dialogs.DeletePlaylistDialog
import com.maxfour.libreplayer.dialogs.RenamePlaylistDialog
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.loaders.PlaylistSongsLoader
import com.maxfour.libreplayer.misc.WeakContextAsyncTask
import com.maxfour.libreplayer.model.AbsCustomPlaylist
import com.maxfour.libreplayer.model.Playlist
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.PlaylistsUtil
import java.util.*

object PlaylistMenuHelper {

    fun handleMenuClick(activity: AppCompatActivity,
                        playlist: Playlist, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                MusicPlayerRemote.openQueue(getPlaylistSongs(activity, playlist), 9, true)
                return true
            }
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(getPlaylistSongs(activity, playlist))
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(getPlaylistSongs(activity, playlist))
                        .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(getPlaylistSongs(activity, playlist))
                return true
            }
            R.id.action_rename_playlist -> {
                RenamePlaylistDialog.create(playlist.id.toLong())
                        .show(activity.supportFragmen