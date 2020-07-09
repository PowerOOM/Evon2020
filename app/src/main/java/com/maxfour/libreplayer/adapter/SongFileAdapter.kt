
package com.maxfour.libreplayer.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.MediaStoreSignature
import com.maxfour.appthemehelper.util.ATHUtil
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.adapter.base.AbsMultiSelectAdapter
import com.maxfour.libreplayer.adapter.base.MediaEntryViewHolder
import com.maxfour.libreplayer.glide.audiocover.AudioFileCover
import com.maxfour.libreplayer.interfaces.CabHolder
import com.maxfour.libreplayer.util.PlayerUtil
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class SongFileAdapter(
		private val activity: AppCompatActivity,
		private var dataSet: List<File>?,
		private val itemLayoutRes: Int,
		private val callbacks: Callbacks?,
		cabHolder: CabHolder?
) : AbsMultiSelectAdapter<SongFileAdapter.ViewHolder, File>(
		activity, cabHolder, R.menu.menu_media_selection
), FastScrollRecyclerView.SectionedAdapter {

	init {
		this.setHasStableIds(true)
	}

	override fun getItemViewType(position: Int): Int {
		return if (dataSet!![position].isDirectory) FOLDER else FILE
	}

	override fun getItemId(position: Int): Long {
		return dataSet!![position].hashCode().toLong()
	}

	fun swapDataSet(songFiles: List<File>) {
		this.dataSet = songFiles
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, index: Int) {
		val file = dataSet!![index]
		holder.itemView.isActivated = isChecked(file)
		holder.title?.text = getFileTitle(file)
		if (holder.text != null) {
			if (holder.itemViewType == FILE) {
				holder.text?.text = getFileText(file)
			} else {
				holder.text?.visibility = View.GONE
			}
		}

		if (holder.image != null) {
			loadFileImage(file, holder)
		}
	}

	private fun getFileTitle(file: File): String {
		return file.name
	}

	private fun getFileText(file: File): String? {
		return if (file.isDirectory) null else readableFileSize(file.length())
	}

	private fun loadFileImage(file: File, holder: ViewHolder) {
		val iconColor = ATHUtil.resolveColor(activity, R.attr.colorControlNormal)
		if (file.isDirectory) {
			holder.image?.let {
				it.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
				it.setImageResource(R.drawable.ic_folder_white_24dp)
			}
			holder.imageTextContainer?.setCardBackgroundColor(ATHUtil.resolveColor(activity, R.attr.colorSurface))

		} else {
			val error = PlayerUtil.getTintedVectorDrawable(
					activity, R.drawable.ic_file_music_white_24dp, iconColor
			)
			Glide.with(activity).load(AudioFileCover(file.path))
					.diskCacheStrategy(DiskCacheStrategy.NONE).error(error).placeholder(error)
					.animate(android.R.anim.fade_in)
					.signature(MediaStoreSignature("", file.lastModified(), 0)).into(holder.image)
		}
	}

	override fun getItemCount(): Int {
		return dataSet!!.size
	}

	override fun getIdentifier(position: Int): File? {
		return dataSet!![position]
	}

	override fun getName(`object`: File): String {
		return getFileTitle(`object`)
	}
