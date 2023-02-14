package com.maxfour.appthemehelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.appthemehelper.util.ColorUtil

class ThemeStore @SuppressLint("CommitPrefEdits")
private constructor(private val mContext: Context) : ThemeStorePrefKeys, ThemeStoreInterface {
    private val mEditor: SharedPreferences.Editor

    init {
        mEditor = prefs(mContext).edit()
    }

    override fun activityTheme(@StyleRes theme: Int): ThemeStore {
        mEditor.putInt(ThemeStorePrefKeys.KEY_ACTIVITY_THEME, theme)
        return this
    }

    override fun primaryColor(@ColorInt color: Int): ThemeStore {
        mEditor.putInt(ThemeStorePrefKeys.KEY_PRIMARY_COLOR, color)
        if (autoGeneratePrimaryDark(mContext))
            primaryColorDark(ColorUtil.darkenColor(color))
        return this
    }

    override fun primaryColorRes(@ColorRes colorRes: Int): ThemeStore {
        return primaryColor(ContextCompat.getColor(mContext, colorRes))
    }

    override fun primaryColorAttr(@AttrRes colorAttr: Int): ThemeStore {
        return primaryColor(ATHUtil.resolveColor(mContext, colorAttr))
    }

    override fun primaryColorDark(@ColorInt color: Int): ThemeStore {
        mEditor.putInt(ThemeStorePrefKeys.KEY_PRIMARY_COLOR_DA