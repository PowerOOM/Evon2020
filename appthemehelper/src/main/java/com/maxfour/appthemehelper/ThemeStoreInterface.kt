package com.maxfour.appthemehelper

import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

internal interface ThemeStoreInterface {

    // Activity theme

    fun activityTheme(@StyleRes theme: Int): ThemeStore

    // Primary colors

    fun primaryColor(@ColorInt color: Int): ThemeStore

    fun primaryColorRes(@ColorRes colorRes: Int): ThemeStore

    fun primaryColorAttr(@AttrRes colorAttr: Int): ThemeStore

    fun autoGeneratePrimar