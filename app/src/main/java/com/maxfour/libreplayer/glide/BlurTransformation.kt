package com.maxfour.libreplayer.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.renderscript.*
import androidx.annotation.FloatRange
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.maxfour.libreplayer.BuildConfig
import com.maxfour.libreplayer.helper.StackBlur
import com.maxfour.libreplayer.util.ImageUtil

class BlurTransformation : BitmapTransformation {

    private var context: Context? = null
    private var blurRadius: Float = 0.toFloat()
    private var sampling: Int = 0

    private fun init(builder: Builder) {
        this.context = builder.context
        this.blurRadius = builder.blurRadius
        this.sampling = builder.sampling
    }

    private constructor(builder: Builder) : super(builder.context) {
        init(builder)
    }

    private constructor(builder: Builder, bitmapPool: BitmapPool) : super(bitmapPool) {
        init(builder)
    }

    class Builder(val context: Context) {
        private var bitmapPool: BitmapPool? = null
        var blurRadius = DEFAULT_BLUR_RADIUS
        var sampling: Int = 0

        /**
         * @param blurRadius The radius to use. Must be between 0 and 25. Default is 5.
         * @return the same Builder
         */
        fun blurRadius(@FloatRange(from = 0.0, to = 25.0) blurRadius: Float):