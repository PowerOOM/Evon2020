package com.maxfour.libreplayer.misc;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class DialogAsyncTask<Params, Progress, Result> extends WeakContextAsyncTask<Params, Progress, Result> {
    private final int delay;

    private WeakReference<Dialog> dialogWeakReference;

    private boolean supposedToBeDismissed;

    public DialogAsyncTask(Context context) {
        this(context, 0);
    }

    public DialogAsyncTask(Context context, int showDelay) {
        super(context);
        this.delay = showDelay;
        dialogWeakReference = new WeakReference<>(null);
    }

    @Override
    protected v