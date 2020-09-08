package com.maxfour.libreplayer.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.fragments.base.AbsMusicServiceFragment
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.helper.MusicProgressViewUpdateHelper
import com.maxfour.libreplayer.helper.PlayPauseButtonOnClickHandler
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PlayerUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.ViewUtil
import kotlinx.android.synthetic.main.fragment_mini_player.*
import kotlin.math.abs

open class MiniPlayerFragment : AbsMusicServiceFragment(), MusicProgressViewUpdateHelper.Callback, View.OnClickListener {

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.actionPlayingQueue -> NavigationUtil.goToPlayingQueue(requireActivity())
            R.id.actionNext -> MusicPlayerRemote.playNextSong()
            R.id.actionPrevious -> MusicPlayerRemote.back()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener(FlingPlayBackController(requireContext()))
        setUpMiniPlayer()

        if (PlayerUtil.isTablet()) {
            actionNext.visibility = View.VISIBLE
            actionPrevious.visibility = View.VISIBLE
            actionNext?.visibility = View.VISIBLE
            actionPrevious?.visibility = View.VISIBLE
            actionPlayingQueue.visibility = View.VISIBLE
        } else {
            actionNext.visibility = if (PreferenceUtil.getInstance(requireContext()).isExtraControls) View.VISIBLE else View.GONE
            actionPlayingQueue.visibility = if (PreferenceUtil.getInstance(requireContext()).isExtraControls) View.GONE else View.VISIBLE
            actionPrevious.visibility = if (PreferenceUtil.getInstance(requireContext()).isExtraControls) View.VISIBLE else View.GONE
        }

        actionPlayingQueue.setOnClickListener(this)
        actionNext.setOnClickListener(this)
        actionPrevious.setOnClickListener(this)
        actionNext?.setOnClickListener(this)
        actionPrevious?.setOnClickListener(this)

    }

    private fun setUpMiniPlayer() {
        setUpPlayPauseButton()
        ViewUtil.setProgressDrawable(progressBar, ThemeStore.accentColor(requireContext()))
    }

    private fun setUpPlayPauseButton() {
        miniPlayerPlayPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    private fun updateSongTitle() {
        val builder = SpannableStringBuilder()

        val song = MusicPlayerRemote.currentSong
        val title = SpannableString(song.title)
        title.setSpan(ForegroundColorSpan(ThemeStore.textColorPrimary(requireContext())), 0, title.length, 0)

        val text = SpannableString(song.artistName)
        text.setSpan(ForegroundColorSpan(ThemeStore.textColorSecondary(requireContext())), 0, text.length, 0)

        builder.append(title).append(" • ").append(text)

        miniPlayerTitle.isSelected = true
        miniPlayerTitle.text = builder
    }

    ov