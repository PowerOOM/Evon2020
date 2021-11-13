package com.maxfour.libreplayer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.maxfour.libreplayer.R;

public class NetworkImageView extends CircularImageView {

    public NetworkImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public NetworkImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NetworkImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    publ