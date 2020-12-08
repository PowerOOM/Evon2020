package com.maxfour.libreplayer.glide;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.maxfour.libreplayer.App;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.glide.artistimage.ArtistImage;
import com.maxfour.libreplayer.glide.palette.BitmapPaletteTranscoder;
import com.maxfour.libreplayer.glide.palette.BitmapPaletteWrapper;
import com.maxfour.libreplayer.model.Artist;
import com.maxfour.libreplayer.util.ArtistSignatureUtil;
import com.maxfour.libreplayer.util.CustomArtistImageUtil;

public class ArtistGlideRequest {

    public static final int DEFAULT_ANIMATION = android.R.anim.fade_in;
    private static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.SOURCE;
    private static final int DEFAULT_ERROR_IMAGE = R.drawable.default_artist_art;

    public static DrawableTypeRequest createBaseRequest(RequestManager requestManager, Artist artist, boolean noCustomImage, boolean forceDownload) {
        boolean hasCustomImage = CustomArtistImageUtil.Companion.getInstance(App.Companion.getContext()).hasCustomArtistImage(artist);
        if (noCustomImage || !hasCustomImage) {
            return requestManager.load(new ArtistImage(artist.getName(), forceDownload));
        } else {
            return requestManager.load(CustomArtistImageUtil.getFile(artist));
        }
    }

    public static Key createSignature(Artist artist) {
        return ArtistSignatureUtil.getInstance(App.Companion.getContext()).getArtistSignature(artist.getName());
    }

    public static class Builder {
        final RequestManager requestManager;
        final Artist artist;
        boolean noCustomImage;
        boolean forceDownload;

        private Builder(@NonNull RequestManager requestManager, Artist artist) {
            this.requestManager = requestManager;
            this.artist = artist;
        }

        public static Builder from(@NonNull RequestManager requestManager, Artist artist) {
            return new Builder(requestManager, artist);
        }

        public PaletteBuilder generatePalette(Context context) {
            return new Pa