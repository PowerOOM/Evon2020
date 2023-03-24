package com.maxfour.appthemehelper.util

import android.os.Build

object VersionUtils {

    /**
     * @return true if device is running API >= 21
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    /**
     * @return true if device is running API >= 23
