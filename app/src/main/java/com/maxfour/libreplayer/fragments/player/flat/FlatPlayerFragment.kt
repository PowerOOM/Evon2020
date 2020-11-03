package com.maxfour.libreplayer.fragments.player.flat

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.appthemehelper.util.ToolbarContentTintHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.fragments.base.AbsPlayerFragment
import com.maxfour.libreplayer.fragments.player.PlayerAlbumCoverFragment
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.model.Song
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.ViewUtil
import com.maxfour.libreplayer.views.DrawableGradient
import kotlinx.android.synthetic.main.fragment_flat_player.*

class FlatPlayerFragment : AbsPlayerFragment() {
    override fun playerToolbar(): Toolbar {
        return playerToolbar
    }

    private var valueAnimator: ValueAnimator? = null
    private lateinit var flatPlaybackControlsFragment: FlatPlaybackControlsFragment
    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor

    private fun setUpSubFragments() {
        flatPlaybackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as FlatPlaybackControlsFragment
        val playerAlbumCoverFragment = childFragmentManager.findFragmentById(R.id.playerAlbumCoverFragment) as PlayerAlbumCoverFragment
        playerAlbumCoverFragm