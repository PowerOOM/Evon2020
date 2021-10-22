package com.maxfour.libreplayer.util;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.StateSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
    public static final boolean USE_FRAMEWORK_RIPPLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private static final int[] PRESSED_STATE_SET = {
            android.R.attr.state_pressed,
    };
    private static final int[] HOVERED_FOCUSED_STATE_SET = {
            android.R.attr.state_hovered, android.