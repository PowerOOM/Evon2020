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

    public static ColorStateList convertToRippleDrawableColor(@Nullable ColorStateList rippleColor) {
        if (USE_FRAMEWORK_RIPPLE) {
            int size = 2;

            final int[][] states = new int[size][];
            final int[] colors = new int[size];
            int i = 0;

            // Ideally we would define a different composite color for each state, but that causes the
            // ripple animation to abort prematurely.
            // So we only allow two base states: selected, and non-selected. For each base state, we only
            // base the ripple composite on its pressed state.

            // Selected base state.
            states[i] = SELECTED_STATE_SET;
            colors[i] = getColorForState(rippleColor, SELECTED_PRESSED_STATE_SET);
            i++;

            // Non-selected base state.
            states[i] = StateSet.NOTHING;
            colors[i] = getColorForState(rippleColor, PRESSED_STATE_SET);
            i++;

            return new ColorStateList(states, colors);
        } else {
            int size = 10;

            final int[][] states = new int[size][];
            final int[] colors = new int[size];
            int i = 0;

            states[i] = SELECTED_PRESSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, SELECTED_PRESSED_STATE_SET);
            i++;

            states[i] = SELECTED_HOVERED_FOCUSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, SELECTED_HOVERED_FOCUSED_STATE_SET);
            i++;

            states[i] = SELECTED_FOCUSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, SELECTED_FOCUSED_STATE_SET);
            i++;

            states[i] = SELECTED_HOVERED_STATE_SET;
            colors[i] = getColorForState(rippleColor, SELECTED_HOVERED_STATE_SET);
            i++;

            // Checked state.
            states[i] = SELECTED_STATE_SET;
            colors[i] = Color.TRANSPARENT;
            i++;

            states[i] = PRESSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, PRESSED_STATE_SET);
            i++;

            states[i] = HOVERED_FOCUSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, HOVERED_FOCUSED_STATE_SET);
            i++;

            states[i] = FOCUSED_STATE_SET;
            colors[i] = getColorForState(rippleColor, FOCUSED_STATE_SET);
            i++;

            states[i] = HOVERED_STATE_SET;
            colors[i] = getColorForState(rippleColor, HOVERED_STATE_SET);
            i++;

            // Defau