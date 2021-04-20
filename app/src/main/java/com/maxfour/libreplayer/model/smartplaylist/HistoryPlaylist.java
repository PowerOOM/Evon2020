package com.maxfour.libreplayer.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.loaders.TopAndRecentlyPlayedSongsLoader;
import com.maxfour.libreplayer.model.Song;
import com.maxfour.libreplayer.providers.HistoryStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryPlaylist extends AbsSmartPlaylist {

    public static final Creator<HistoryPlaylist> CREATOR = new Creator<HistoryPlaylist>() {
        public HistoryPlaylist createFromParcel(Parcel so