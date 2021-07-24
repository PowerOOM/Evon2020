package com.maxfour.libreplayer.preferences

import android.app.Dialog
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.CategoryInfoAdapter
import com.maxfour.libreplayer.model.CategoryInfo
import com.maxfour.libreplayer.util.PreferenceUtil
import java.util.*

class LibraryPreference : ATEDialogPreference {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        icon?.setColorFilter(ThemeStore.textColorSecondary(context), PorterDuff.Mode.SRC_IN)
    }
}

class LibraryPreferenceDialog : PreferenceDialogFragmentCompat() {

    override fun onDialogClosed(positiveResult: Boolean) {

    }

    lateinit var adapter: CategoryInfoAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.preference_dialog_lib