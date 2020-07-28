package com.maxfour.libreplayer.appshortcuts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.maxfour.libreplayer.activities.SearchActivity
import com.maxfour.libreplayer.appshortcuts.shortcuttype.LastAddedShortcutType
import com.maxfour.libreplayer.appshortcuts.shortcuttype.SearchShortCutType
import com.maxfour.libreplayer.appshortcuts.shortcuttype.ShuffleAllShortcutType
import com.maxfour.libreplayer.appshortcuts.shortcuttype.TopSongsShortcutType
import com.maxfour.libreplayer.model.Playlist
import com.maxfour.libreplayer.model.smartplaylist.LastAddedPlaylist
import com.maxfour.libreplayer.model.smartplaylist.MyTopSongsPlaylist
import com.maxfour.libreplayer.model.smartplaylist.ShuffleAllPlaylist
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.service.MusicService.*

class AppShortcutLauncherActivity : Activity() {

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		var shortcutType = SHORTCUT_TYPE_NONE

		// Set shortcutType from the intent extras
		val extras = intent.extras
		if (extras != null) {
			shortcutType = extras.getInt(KEY_SHORTCUT_TYPE, SHORTCUT_TYPE_NONE)
		}

		when (shortcutType) {
			SHORTCUT_TYPE_SHUFFLE_ALL -> {
				startServiceWithPlaylist(
						Musi