package com.maxfour.libreplayer.activities.tageditor

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.maxfour.appthemehelper.util.MaterialUtil
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.extensions.appHandleColor
import com.maxfour.libreplayer.extensions.applyToolbar
import com.maxfour.libreplayer.loaders.SongLoader
import kotlinx.android.synthetic.main.activity_song_tag_editor.*
import org.jaudiotagger.tag.FieldKey
import java.util.*

class SongTagEditorActivity : AbsTagEditorActivity(), TextWatcher {

	override val contentViewLayout: Int
		get() = R.layout.activity_song_tag_editor

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setNoImageMode()
		setUpViews()
		applyToolbar(toolbar)
	}

	private fun setUpViews() {
		fillViewsWithFileTags()
		MaterialUtil.setTint(songTextContainer, false)
		MaterialUtil.setTint(composerContainer, false)
		MaterialUtil.setTint(albumTextContainer, false)
		MaterialUtil.setTint(artistContainer, false)
		MaterialUtil.setTint(albumArtistContainer, false)
		MaterialUtil.setTint(yearContainer, false)
		MaterialUtil.setTint(genreContainer, false)
		MaterialUtil.setTint(songNumberContainer, false)
		MaterialUtil.setTint(lyricsContainer, false)

		songText.appHandleColor().addTextChangedListener(this)
		albumText.appHandleColor().addTextChangedListener(this)
		albumArtistText.appHandleColor().addTextChangedListener(this)
		artistText.appHandleColor().addTextChangedListener(this)
		genreText.appHandleColor().addTextChangedListener(this)
		yearText.appHandleColor().addTextChangedListener(this)
		songNumberText.appHandleColor().addTextChangedListener(this)
		lyricsText.appHandleColor().addTextChangedListener(this)
		songComposerText.appHandleColor().addTextChangedListener(this)
	}

	private fun fillViewsWithFileTags() {
		songText.setText(songTitle)
		albumArtistText.setText(albumArtist)
		albumText.setText(albumTitle)
		artistText.setText(artistName)
		genreText.setText(genreName)
		yearText.setText(songYear)
		songNumberText.setText(songNumber)
		lyricsText.setText(lyr