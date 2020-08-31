package com.maxfour.libreplayer.dialogs

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.util.TintHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.service.MusicService
import com.maxfour.libreplayer.service.MusicService.ACTION_PENDING_QUIT
import com.maxfour.libreplayer.service.MusicService.ACTION_QUIT
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.PreferenceUtil
import com.maxfour.libreplayer.util.ViewUtil

class SleepTimerDialog : DialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater
    private lateinit var materialDialog: MaterialDialog
    private lateinit var shouldFinishLastSong: CheckBox
    private lateinit var seekBar: SeekBar
    private lateinit var timerDisplay: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timerUpdater = TimerUpdater()

        materialDialog = MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                .title(R.string.action_sleep_timer)
                .cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                .positiveButton(R.string.action_set) {
                    PreferenceUtil.getInstance(requireContext()).sleepTimerFinishMusic = shouldFinishLastSong.isChecked

                    val minutes = seekArcProgress

                    val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)

                    val nextSleepTimerElapsedTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000
                    PreferenceUtil.getInstance(requireContext()).setNextSleepTimerElapsedRealtime(nextSleepTimerElapsedTime)
                    val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextSleepTimerElapsedTime, pi)

                    Toast.makeText(activity, activity!!.resources.getQuantityString(R.plurals.sleep_timer_set, minutes, minutes), Toast.LENGTH_SHORT).show()
                }
                .negativeButton(android.R.string.cancel) {
                    if (activity == null) {
                        return@negativeButton
                    }
                    val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                    if (previous != null) {
                        val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        am.cancel(previous)
                        previous.cancel()
                        Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show()
                    }

                    val musicService = MusicPlayerRemote.musicService
                    if (musicService != null && musicService.pendingQuit) {
                        musicService.pendingQuit = false
                        Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show()
                    }
                }
                .customView(R.layout.dialog_sleep_timer, scrollable = false)
                .show {
                    onShow {
                     