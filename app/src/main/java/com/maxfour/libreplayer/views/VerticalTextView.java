package com.maxfour.libreplayer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;


public class VerticalTextView extends AppCompatTextView {
    final boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(contex