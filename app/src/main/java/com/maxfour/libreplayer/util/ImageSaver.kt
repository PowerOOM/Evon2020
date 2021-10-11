package com.maxfour.libreplayer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ImageSaver(val context: Context) {
    private var external: Boolean = false
    private var directoryName: String = "LibrePlayer"
    private var fileName: String = "profile.png"

    fun setFileName(fileName: String): ImageSaver {
        this.fileName = fileName
        return this
    }

    fun setDirectoryName(directoryName: String): ImageSaver {
        this.directoryName = directoryName
        return this
    }

    fun setStoreType