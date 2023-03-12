package com.maxfour.appthemehelper.common.prefs.supportv7

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.preference.PreferenceViewHolder
import androidx.preference.SeekBarPreference
import com.maxfour.appthemehelper.R
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.util.TintHelper

class ATESeekBarPreference : SeekBarPreference {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
