package com.maxfour.libreplayer.misc;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LagTracker {
    private static Map<String, Long> mMap;
    private static LagTracker mSingleton;
    private boolean mEnabled = true;

    private LagTracker() {
        mMap = new HashMap();
    }

    public static LagTracker get() {
        if (mSingleton == null) {
            mSingleton = new LagTracker();
        }
        return mSingleton;
    }

    private void print(String str, long j) {
        long toMillis = TimeUnit.NANOSECONDS.toMillis(j);
        Log.d("LagTracker", "