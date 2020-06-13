
package com.maxfour.libreplayer.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import com.maxfour.appthemehelper.ThemeStore
import com.maxfour.appthemehelper.util.ColorUtil
import com.maxfour.appthemehelper.util.MaterialValueHelper
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.activities.base.AbsMusicServiceActivity
import com.maxfour.libreplayer.adapter.song.PlayingQueueAdapter
import com.maxfour.libreplayer.extensions.applyToolbar
import com.maxfour.libreplayer.helper.MusicPlayerRemote
import com.maxfour.libreplayer.util.MusicUtil
import com.maxfour.libreplayer.util.ViewUtil
import kotlinx.android.synthetic.main.activity_playing_queue.*

open class PlayingQueueActivity : AbsMusicServiceActivity() {

	private var wrappedAdapter: RecyclerView.Adapter<*>? = null
	private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
	private var recyclerViewSwipeManager: RecyclerViewSwipeManager? = null
	private var recyclerViewTouchActionGuardManager: RecyclerViewTouchActionGuardManager? = null
	private var playingQueueAdapter: PlayingQueueAdapter? = null
	private lateinit var linearLayoutManager: LinearLayoutManager

	private fun getUpNextAndQueueTime(): String {
		val duration = MusicPlayerRemote.getQueueDurationMillis(MusicPlayerRemote.position)
		return MusicUtil.buildInfoString(
				resources.getString(R.string.up_next),
				MusicUtil.getReadableDurationString(duration)
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		setDrawUnderStatusBar()
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_playing_queue)
		setStatusbarColorAuto()
		setNavigationbarColorAuto()
		setTaskDescriptionColorAuto()
		setLightNavigationBar(true)

		setupToolbar()
		setUpRecyclerView()

		clearQueue.setOnClickListener {
			MusicPlayerRemote.clearQueue()
		}
		checkForPadding()
	}
