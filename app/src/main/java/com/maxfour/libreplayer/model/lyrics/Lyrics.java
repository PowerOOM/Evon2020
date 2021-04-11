package com.maxfour.libreplayer.model.lyrics;


import com.maxfour.libreplayer.model.Song;

import java.util.ArrayList;

public class Lyrics {
    private static final ArrayList<Class<? extends Lyrics>> FORMATS = new ArrayList<>();

    static {
        Lyrics.FORMATS.add(SynchronizedLyricsLRC.class);
    }

    public Song song;
    public String data;
    protected boolean parsed = false;
    protected boolean valid = false;

    public static Lyrics parse(Song so