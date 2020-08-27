package com.maxfour.libreplayer.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.loaders.PlaylistLoader
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.PlaylistsUtil
import com.maxfour.libreplayer.util.PreferenceUtil

class AddToPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(
            savedInstanceState: Bundle?
    ): Dialog {
        val playlists = PlaylistLoader.getAllPlaylists(requireContext())
        val playlistNames: MutableList<String> = mutableListOf()
        playlistNames.add(requireContext().resources.getString(R.string.action_new_playlist))
        for (p in playlists) {
            playlistNames.add(p.name)
        }

        return MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title