package com.maxfour.libreplayer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.appwidgets.AppWidgetBig;
import com.maxfour.libreplayer.appwidgets.AppWidgetCard;
import com.maxfour.libreplayer.appwidgets.AppWidgetClassic;
import com.maxfour.libreplayer.appwidgets.AppWidgetSmall;
import com.maxfour.libreplayer.appwidgets.AppWidgetText;
import com.maxfour.libreplayer.glide.BlurTransformation;
import com.maxfour.libreplayer.glide.SongGlideRequest;
import com.maxfour.libreplayer.helper.ShuffleHelper;
import com.maxfour.libreplayer.model.Playlist;
import com.maxfour.libreplayer.model.Song;
import com.maxfour.libreplayer.providers.HistoryStore;
import com.maxfour.libreplayer.providers.MusicPlaybackQueueStore;
import com.maxfour.libreplayer.providers.SongPlayCountStore;
import com.maxfour.libreplayer.service.notification.PlayingNotification;
import com.maxfour.libreplayer.service.notification.PlayingNotificationImpl;
import com.maxfour.libreplayer.service.notification.PlayingNotificationOreo;
import com.maxfour.libreplayer.service.playback.Playback;
import com.maxfour.libreplayer.util.MusicUtil;
import com.maxfour.libreplayer.util.PlayerUtil;
import com.maxfour.libreplayer.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MusicService extends Service implements
        SharedPreferences.OnSharedPreferenceChangeListener, Playback.PlaybackCallbacks {
    public static final String TAG = MusicService.class.getSimpleName();

    public static final String MUSIC_PACKAGE_NAME = "com.android.music";

    public static final String ACTION_TOGGLE_PAUSE = MUSIC_PACKAGE_NAME + ".togglepause";
    public static final String ACTION_PLAY = MUSIC_PACKAGE_NAME + ".play";
    public static final String ACTION_PLAY_PLAYLIST = MUSIC_PACKAGE_NAME + ".play.playlist";
    public static final String ACTION_PAUSE = MUSIC_PACKAGE_NAME + ".pause";
    public static final String ACTION_STOP = MUSIC_PACKAGE_NAME + ".stop";
    public static final String ACTION_SKIP = MUSIC_PACKAGE_NAME + ".skip";
    public static final String ACTION_REWIND = MUSIC_PACKAGE_NAME + ".rewind";
    public static final String ACTION_QUIT = MUSIC_PACKAGE_NAME + ".quitservice";
    public static final String ACTION_PENDING_QUIT = MUSIC_PACKAGE_NAME + ".pendingquitservice";
    public static final String INTENT_EXTRA_PLAYLIST = MUSIC_PACKAGE_NAME + "intentextra.playlist";
    public static final String INTENT_EXTRA_SHUFFLE_MODE = MUSIC_PACKAGE_NAME + ".intentextra.shufflemode";

    public static final String APP_WIDGET_UPDATE = MUSIC_PACKAGE_NAME + ".appwidgetupdate";
    public static final String EXTRA_APP_WIDGET_NAME = MUSIC_PACKAGE_NAME + "app_widget_name";

    // Do not change these three strings as it will break support with other apps (e.g. last.fm scrobbling)
    public static final String META_CHANGED = MUSIC_PACKAGE_NAME + ".metachanged";
    public static final String QUEUE_CHANGED = MUSIC_PACKAGE_NAME + ".queuechanged";
    public static final String PLAY_STATE_CHANGED = MUSIC_PACKAGE_NAME + ".playstatechanged";

    public static final String FAVORITE_STATE_CHANGED = MUSIC_PACKAGE_NAME + "favoritestatechanged";

    public static final String REPEAT_MODE_CHANGED = MUSIC_PACKAGE_NAME + ".repeatmodechanged";
    public static final String SHUFFLE_MODE_CHANGED = MUSIC_PACKAGE_NAME + ".shufflemodechanged";
    public static final String MEDIA_STORE_CHANGED = MUSIC_PACKAGE_NAME + ".mediastorechanged";

    public static final String CYCLE_REPEAT = MUSIC_PACKAGE_NAME + ".cyclerepeat";
    public static final String TOGGLE_SHUFFLE = MUSIC_PACKAGE_NAME + ".toggleshuffle";
    public static final String TOGGLE_FAVORITE = MUSIC_PACKAGE_NAME + ".togglefavorite";

    public static final String SAVED_POSITION = "POSITION";
    public static final String SAVED_POSITION_IN_TRACK = "POSITION_IN_TRACK";
    public static final String SAVED_SHUFFLE_MODE = "SHUFFLE_MODE";
    public static final String SAVED_REPEAT_MODE = "REPEAT_MODE";

    public static final int RELEASE_WAKELOCK = 0;
    public static final int SONG_ENDED = 1;
    public static final int SONG_WENT_TO_NEXT = 2;
    public static final int PLAY_SONG = 3;
    public static final int PREPARE_NEXT = 4;
    public static final int SET_POSITION = 5;
    public static final int FOCUS_CHANGE = 6;
    public static final int DUCK = 7;
    public static final int UNDUCK = 8;
    public static final int RESTORE_QUEUES = 9;

    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int SHUFFLE_MODE_SHUFFLE = 1;

    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ALL = 1;
    public 