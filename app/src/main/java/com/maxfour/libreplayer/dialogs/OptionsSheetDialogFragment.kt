package com.maxfour.libreplayer.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.MainActivity
import com.maxfour.libreplayer.util.NavigationUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.views.OptionMenuItemView

class OptionsSheetDialogFragment : DialogFragment(), View.OnClickListener {

    override fun onClick(view: View) {
        val mainActivity = activity as MainActivity? ?: return
        when (view.id) {
            R.id.actionFolders -> mainActivity.setMusicChooser(MainActivity.FOLDER)
            R.id.actionLibrary -> mainActivity.setMusicChooser(MainActivity.LIBRARY)
            R.id.actionSettings -> NavigationUtil.goToSettings(mainActivity)
        }
        materialDialog.dismiss()
    }

    private lateinit var actionSettings: OptionMenuItemView
    private lateinit var actionLibrary: OptionMenuItemView
    private lateinit var actionFolders: OptionMenuItemView
    private lateinit var materialDialog