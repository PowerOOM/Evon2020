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

public class Mu