package com.maxfour.libreplayer.fragments.settings

import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.TwoStatePreference
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.colorChooser
import com.maxfour.appthemehelper.ACCENT_COLORS
import com.maxfour.appthemehelper.ACCENT_COLORS_SUB
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.common.prefs.supportv7.ATEColorPreference
import com.maxfour.appthemehelper.common.prefs.supportv7.ATESwitchPreference
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.VersionUtils
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.appshortcuts.DynamicShortcutManager
import com.maxfour.libreplayer.util.PreferenceUtil

class ThemeSettingsFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val generalTheme: Preference? = findPreference("general_theme")
        generalTheme?.let {
   