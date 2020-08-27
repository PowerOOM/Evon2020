
package com.maxfour.libreplayer.dialogs

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.util.PreferenceUtil
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class BlacklistFolderChooserDialog : DialogFragment() {

    private val initialPath = Environment.getExternalStorageDirectory().absolutePath
    private var parentFolder: File? = null
    private var parentContents: Array<File>? = null
    private var canGoUp = false
    private var callback: FolderCallback? = null


    private fun contentsArray(): List<String> {