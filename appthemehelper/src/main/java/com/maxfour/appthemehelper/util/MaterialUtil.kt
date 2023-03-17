package com.maxfour.appthemehelper.util

import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatButton
import com.afollestad.materialdialogs.internal.button.DialogActionButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.maxfour.appthemehelper.ThemeStore


object MaterialUtil {

    @JvmOverloads
    fun setTint(button: MaterialButton, background: Boolean = true,
                color: Int = ThemeStore.accentColor(button.context)) {

        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor = ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(context, ColorUt