package com.maxfour.libreplayer.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.SettingsActivity
import com.maxfour.libreplayer.util.NavigationUtil
import kotlinx.android.synthetic.main.fragment_main_settings.*

class MainSettingsFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.generalSettings -> inflateFragment(ThemeSettingsFragment(), R.string.general_settings_title)
            R.id.audioSettings -> inflateFragment(AudioSettings(), R.string.pref_header_audio)
            R.id.nowPlayingSettings -> inflateFragment(NowPlayingSettingsFragment(), R.strin