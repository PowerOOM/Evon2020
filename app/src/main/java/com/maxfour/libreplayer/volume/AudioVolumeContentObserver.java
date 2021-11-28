package com.maxfour.libreplayer.volume;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

public class AudioVolumeContentObserver extends ContentObserver {

    private final OnAudioVolumeChangedListener mListener;
    private final AudioManager mAudioManager;
    private final int mAudioStreamType;
    private int mLastVolume;

    AudioVolumeContentObserver(@NonNull Handler handler, @NonNull AudioManager audioManager,
                               int audioStreamType,
                               @NonNull OnAudioVolumeChangedListener listener) {

        super(handler);
        mAudioManager =