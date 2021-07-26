package com.maxfour.libreplayer.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maxfour.libreplayer.loaders.SongLoader;
import com.maxfour.libreplayer.model.Song;

import java.util.ArrayList;

import io.reactivex.Observable;

public class MusicPlaybackQueueStore extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "music_playback_state.db";
    public static final String PLAYING_QUEUE_TABLE_NAME = "playing_queue";
    public static final String ORIGINAL_PLAYING_QUEUE_TABLE_NAME = "original_playing_queue";
    private static final int VERSION = 10;
    @Nullable
    private static MusicPlaybackQueueStore sInstance = null;

    /**
     * Constructor of <code>MusicPlaybackState</code>
     *
     * @param context The {@link Context} to use
     */
    public MusicPlaybackQueueStore(final @NonNull Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * @param context The {@link Context} to use
     * @return A new instance of this class.
     */
    @NonNull
    public static synchronized MusicPlaybackQueueStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new MusicPlaybackQueueStore(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        createTable(db, PLAYING_QUEUE_TABLE_NAME);
        createTable(db, ORIGINAL_PLAYING_QUEUE_TABLE_NAME);
    