package com.maxfour.libreplayer.volume;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import androidx.annotation.NonNull;

public class AudioVolumeObserver {

    private final Context mContext;
    private final AudioManager mAudioManager;
    private AudioVolumeContentObserver mAudioVolumeContentOb