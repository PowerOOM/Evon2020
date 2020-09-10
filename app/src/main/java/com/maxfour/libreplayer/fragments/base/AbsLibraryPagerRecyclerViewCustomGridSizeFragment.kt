package com.maxfour.libreplayer.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.maxfour.libreplayer.R
import com.maxfour.libreplayer.util.PlayerUtil

abstract class AbsLibraryPagerRecyclerViewCustomGridSizeFragment<A : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> :
    AbsLibraryPagerRecyclerViewFragment<A, LM>() {

    private var gridSize: Int = 0
    private var sortOrder: String? = null

    private var usePaletteInitialized: Boolean = false
    private var usePalette: Boolean = false
    private var currentLayoutRes: Int = 0

    val maxGridSize: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else {
            resources.getInteger(R.integer.max_columns)
        }

    /**
     * Override to customize which item layout currentLayoutRes should be used. You might also want to
     * override [.canUsePalette] then.
     *
     * @see .getGridSize
     */
    protected val itemLayoutRes: Int
        get() = if (getGridSize() > maxGridSizeForList) {
            R.layout.item_grid
        } else R.layout.item_list

    protected val maxGridSizeForList: Int
        get() = if (isLandscape) {
            activity!!.resources.getInteger(R.integer.default_list_columns_land)
        } else activity!!.resources.getInteger(R.integer.default_list_columns)

    private val isLandscape: Boolean
        get() = PlayerUtil.isLandscape()

    fun getGridSize(): Int {
        if (gridSize == 0) {
            gridSize = if (isLandscape) {
                loadGridSizeLand()
            } else {
                loadGridSize()
            }
        }
        return gridSize
    }

    protected abstract fun setGridSize(gridSize: Int)

    fun getSortOrder(): String? {
        if (sortOrder == null) {
            sortOrder = loadSortOrder()
        }
        return sortOrder
    }

    protected abstract fun setSortOrder(sortOrder: String)

    fun setAndSaveSortOrder(sortOrder: String) {
        this.sortOrder = sortOrder
        saveSortOrder(sortOrder)
        setSortOrder(sortOrder)
    }

    /**
     * @return whether the palette should be used at all or not
     */
    fun usePalette(): Bool