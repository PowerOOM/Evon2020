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
            android.R.attr.state_hovered, android.R.attr.state_focused,
    };
    private static final int[] FOCUSED_STATE_SET = {
            android.R.attr.state_focused,
    };
    private static final int[] HOVERED_STATE_SET = {
            android.R.attr.state_hovered,
    };

    private static final int[] SELECTED_PRESSED_STATE_SET = {
            android.R.attr.state_selected, android.R.attr.state_pressed,
    };
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET = {
            android.R.attr.state_selected, android.R.attr.state_hovered, android.R.attr.state_focused,
    };
    private static final int[] SELECTED_FOCUSED_STATE_SET = {
            android.R.attr.state_selected, android.R.attr.state_focused,
    };
    private static final int[] SELECTED_HOVERED_STATE_SET = {
            android.R.attr.state_selected, android.R.attr.state_hovered,
    };
    private static final int[] SELECTED_STATE_SET = {
            android.R.attr.state_selected,
    };

    private static final int[] ENABLED_PRESSED_STATE_SET = {
            android.R.attr.state_enabled, android.R.attr.state_pressed
    };

    public static ColorStateList convertToRippleDrawableColor(@Nullable ColorStateList rippleColor