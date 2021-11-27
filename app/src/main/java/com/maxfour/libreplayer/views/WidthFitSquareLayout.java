package com.maxfour.libreplayer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class WidthFitSquareLayout extends FrameLayout {
    private boolean forceSquare = true;

    public WidthFitSquareLayout(Context context) {
        super(context);
    }

    public WidthFitSquareLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WidthFitSquareLayout(Context context, AttributeSet attribut