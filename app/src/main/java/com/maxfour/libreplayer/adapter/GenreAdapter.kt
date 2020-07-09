package com.maxfour.libreplayer.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.base.MediaEntryViewHolder
import com.maxfour.libreplayer.model.Genre
import com.maxfour.libreplayer.util.NavigationUtil
import java.util.*

class GenreAdapter(
		private val activity: Activity, dataSet: ArrayList<Genre>, private val mItemLayoutRes: Int
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
	var dataSet = ArrayList<Genre>()
		private set

	init {
		this.dataSet = dataSet
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater