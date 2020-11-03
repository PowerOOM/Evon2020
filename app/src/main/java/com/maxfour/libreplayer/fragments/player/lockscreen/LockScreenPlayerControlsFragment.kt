
package com.maxfour.libreplayer.fragments.player.lockscreen

import android.animation.ObjectAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.appthemehelper.util.TintHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.extensions.ripAlpha
import com.maxfour.libreplayer.fragments.base.AbsPlayerControlsFragment
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.MusicProgressViewUpdateHelper
import com.maxfour.libreplayer.helper.PlayPauseButtonOnClickHandler
import com.maxfour.libreplayer.misc.SimpleOnSeekbarChangeListener
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.ViewUtil
import kotlinx.android.synthetic.main.fragment_lock_screen_playback_controls.*

class LockScreenPlayerControlsFragment : AbsPlayerControlsFragment() {

    private var progressViewUpdateHelper: MusicProgressViewUpdateHelper? = null
    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_lock_screen_playback_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMusicControllers()
        title.isSelected = true
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        title.text = song.title
        text.text = String.format("%s - %s", song.artistName, song.albumName)

    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper!!.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper!!.stop()
    }

    override fun onServiceConnected() {
        updatePlayPauseDrawableState()
        updateRepeatState()
        updateShuffleState()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }

    override fun setDark(color: Int) {

        val colorBg = ATHUtil.resolveColor(requireContext(), android.R.attr.colorBackground)
        if (ColorUtil.isColorLight(colorBg)) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(requireContext(), true)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(requireContext(), true)
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(requireContext(), false)
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(requireContext(), false)
        }

        val colorFinal = if (PreferenceUtil.getInstance(requireContext()).adaptiveColor) {
            color
        } else {
            ThemeStore.textColorSecondary(requireContext())
        }.ripAlpha()

        volumeFragment?.setTintable(colorFinal)
        ViewUtil.setProgressDrawable(progressSlider, colorFinal, true)

        updatePrevNextColor()

        val isDark = ColorUtil.isColorLight(colorFinal)
        text.setTextColor(colorFinal)

        TintHelper.setTintAuto(playPauseButton, MaterialValueHelper.getPrimaryTextColor(requireContext(), isDark), false)
        TintHelper.setTintAuto(playPauseButton, colorFinal, true)
    }

    private fun setUpPlayPauseFab() {
        playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_pause_white_24dp)
        } else {
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_32dp)
        }
    }


    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpProgressSlider()
        setUpShuffleButton()
        setUpRepeatButton()
    }
