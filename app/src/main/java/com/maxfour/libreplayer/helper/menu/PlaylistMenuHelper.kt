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
import com.maxfour.libreplayer