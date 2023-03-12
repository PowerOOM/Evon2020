package com.maxfour.appthemehelper.common.prefs.supportv7

import android.annotation.TargetApi
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import androidx.preference.CheckBoxPreference
import com.maxfour.appthemehelper.R
import com.maxfour.appthemehelper.ThemeStore

class ATESwitchPreference : CheckBoxPreference {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyle