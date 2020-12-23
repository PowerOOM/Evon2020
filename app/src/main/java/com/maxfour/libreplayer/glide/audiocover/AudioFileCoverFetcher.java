package com.maxfour.libreplayer.glide.audiocover;

import android.media.MediaMetadataRetriever;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioFileCoverFetcher implements DataFetcher<In