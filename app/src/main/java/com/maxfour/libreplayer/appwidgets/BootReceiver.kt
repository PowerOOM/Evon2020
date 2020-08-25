package com.maxfour.libreplayer.appwidgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.maxfour.libreplayer.service.MusicService

class BootReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		val widgetManager = AppWidgetManager.getInstance(context)

		// Start music service if there are any existing widgets
		if (widgetManager.getAppWidgetIds(
						ComponentName(
								context, AppWidgetBig::class.java
						)
				).isNotEmpt