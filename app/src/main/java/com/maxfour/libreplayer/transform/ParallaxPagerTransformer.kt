package com.maxfour.libreplayer.transform

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import androidx.viewpager.widget.ViewPager

class ParallaxPagerTransformer(private val id: Int) : ViewPager.PageTransformer {
    private var border = 0
    private var speed = 0.2f

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun transformPage(view: View, position: Float) {

        val parallaxView = view.findViewById<View>(id)

        if (parallaxView != null) {
            if (position > -1 && position < 1) {
                val width = parallaxView.width.toFloat()
                parallaxView.translationX = -(position * width * speed)
                val sc