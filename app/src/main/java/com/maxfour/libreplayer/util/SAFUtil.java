package com.maxfour.libreplayer.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.model.Song;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAFUtil {

    public static final String TAG = SAFUtil.class.getSimpleName();
    public static final String SEPARATOR = "###/SAF/###";

    public static final int REQUEST_SAF_PICK_FILE = 42;
    public static final int REQUEST_SAF_PICK_TREE = 43;

    public static boolean isSAFRequired(File file) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !file.canWrite();
    }

    public static boolean isSAFRequired(String path) {
        return isSAFRequired(new File(path));
    }

    public static boolean isSAFRequired(AudioFile audio) {
        return isSAFRequired(audio.getFile());
    }

    public static boolean isSAFRequired(Song song) {
        return isSAFRequired(song.getData());
    }

    public static boolean isSAFRequired(List<String> paths) {
        for (String path : paths) {
            if (isSAFRequired(path)) return true;
        }
        return false;
    }

    public static boolean isSAFRequiredForSongs(List<Song> songs) {
        for (Song song : songs) {
            if (isSAFRequired(song)) return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void openFilePicker(Activity activity) {
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("audio/*");
        i.putExtra("android.content.extra.SHOW_ADVANCED", true);
        activity.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_FILE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void openFilePicker(Fragment fragment) {
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("audio/*");
        i.putExtra("android.content.extra.SHOW_ADVANCED", true);
        fragment.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_FILE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void openTreePicker(Activity activity) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.putExtra("android.content.extra.SHOW_ADVANCED", true);
        activity.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_TREE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void openTreePicker(Fragment fragment) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.putExtra("android.content.extra.SHOW_ADVANCED", true);
        fragment.startActivityForResult(i, SAFUtil.REQUEST_SAF_PICK_TREE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void saveTreeUri(Context context, Intent data) {
        Uri uri = data.getData();
        context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PreferenceUtil.getInstanc