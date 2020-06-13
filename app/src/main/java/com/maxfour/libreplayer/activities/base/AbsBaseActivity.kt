package com.maxfour.libreplayer.activities.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.maxfour.libreplayer.R
import com.maxfour.appthemehelper.ThemeStore

abstract class AbsBaseActivity : AbsThemeActivity() {
	private var hadPermissions: Boolean = false
	private lateinit var permissions: Array<String>
	private var permissionDeniedMessage: String? = null

	open fun getPermissionsToRequest(): Array<String> {
		return arrayOf()
	}

	protected fun setPermissionDeniedMessage(message: String) {
		permissionDeniedMessage = message
	}

	fun getPermissionDeniedMessage(): String {
		return if (permissionDeniedMessage == null) getString(R.string.permissions_denied) else permissionDeniedMessage!!
	}

	private val snackBarContainer: View
		get() = window.decorView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		volumeControlStream = AudioManager.STREAM_MUSIC
		permissions = getPermissionsToRequest()
		hadPermissions = hasPermissions()
		permissionDeniedMessage = null
	}

	override fun onPostCreate(savedInstanceState: Bundle?) {