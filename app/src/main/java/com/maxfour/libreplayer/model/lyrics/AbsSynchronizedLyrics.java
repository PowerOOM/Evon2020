package com.maxfour.libreplayer.model.lyrics;

import android.util.SparseArray;

public abstract class AbsSynchronizedLyrics extends Lyrics {
    private static final int TIME_OFFSET_MS = 500; // time adjustment to display line before it actually starts

    protected final SparseArray<String> lines = new SparseArray<>();
    protected int offset = 0;

    public String getLine(int time) {
        time += offset + AbsSynchronizedLyrics.TIME_OFFSET_MS;

        int lastLineTime = lines.keyAt(0);

        for (int i = 0; i < lines.size(); i++) {
            int lineTime = lines.keyAt(i);

        