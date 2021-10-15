package com.maxfour.libreplayer.util;

import com.maxfour.libreplayer.rest.model.LastFmAlbum.Album.Image;
import com.maxfour.libreplayer.rest.model.LastFmArtist;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LastFMUtil {

    public static String getLargestAlbumImageUrl(List<Image> list) {
        Map hashMap = new HashMap();
        for (Image image : list) {
            Object obj = null;
            String size = image.getSize();
            if 