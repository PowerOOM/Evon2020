package com.maxfour.appthemehelper.util

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.maxfour.appthemehelper.ThemeStore

object NavigationViewUtil {

    fun setItemIconColors(navigationView: NavigationView, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        val iconSl = ColorStateList(arrayOf(intArrayOf(-android.R.a