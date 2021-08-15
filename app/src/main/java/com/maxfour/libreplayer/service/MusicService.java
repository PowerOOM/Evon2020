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
    public static final int REPEAT_MODE_THIS = 2;

    public static final int SAVE_QUEUES = 0;
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;
    private final IBinder musicBind = new MusicBinder();
    public boolean pendingQuit = false;

    @Nullable
    public Playback playback;
    public int position = -1;
    public int nextPosition = -1;
    private AppWidgetBig appWidgetBig = AppWidgetBig.Companion.getInstance();
    private AppWidgetClassic appWidgetClassic = AppWidgetClassic.Companion.getInstance();
    private AppWidgetSmall appWidgetSmall = AppWidgetSmall.Companion.getInstance();
    private AppWidgetCard appWidgetCard = AppWidgetCard.Companion.getInstance();
    private AppWidgetText appWidgetText = AppWidgetText.Companion.getInstance();
    private final BroadcastReceiver widgetIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String command = intent.getStringExtra(EXTRA_APP_WIDGET_NAME);
            final int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (command != null) {
                switch (command) {
                    case AppWidgetClassic.NAME: {
                        appWidgetClassic.performUpdate(MusicService.this, ids);
                        break;
                    }
                    case AppWidgetSmall.NAME: {
                        appWidgetSmall.performUpdate(MusicService.this, ids);
                        break;
                    }
                    case AppWidgetBig.NAME: {
                        appWidgetBig.performUpdate(MusicService.this, ids);
                        break;
                    }
                    case AppWidgetCard.NAME: {
                        appWidgetCard.performUpdate(MusicService.this, ids);
                        break;
                    }
                    case AppWidgetText.NAME: {
                        appWidgetText.performUpdate(MusicService.this, ids);
                        break;
                    }
                }
            }

        }
    };
    private ArrayList<Song> playingQueue = new ArrayList<>();
    private ArrayList<Song> originalPlayingQueue = new ArrayList<>();
    private int shuffleMode;
    private int repeatMode;
    private boolean queuesRestored;
    private boolean pausedByTransientLossOfFocus;
    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                pause();
            }
        }
    };
    private PlayingNotification playingNotification;
    private final BroadcastReceiver updateFavoriteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            updateNotification();
        }
    };
    private AudioManager audioManager;
    private MediaSessionCompat mediaSession;
    private PowerManager.WakeLock wakeLock;
    private PlaybackHandler playerHandler;
    private final AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(final int focusChange) {
            playerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };
    private QueueSaveHandler queueSaveHandler;
    private HandlerThread musicPlayerHandlerThread;
    private HandlerThread queueSaveHandlerThread;
    private SongPlayCountHelper songPlayCountHelper = new SongPlayCountHelper();
    private ThrottledSeekHandler throttledSeekHandler;
    private boolean becomingNoisyReceiverRegistered;
    private IntentFilter becomingNoisyReceiverIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private ContentObserver mediaStoreObserver;
    private boolean notHandledMetaChangedForCurrentSong;
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //Not in call: Play music
                    play();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //A call is dialing, active or on hold
                    pause();
                    break;
                default:
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };
    private Handler uiThreadHandler;
    private IntentFilter headsetReceiverIntentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private boolean headsetReceiverRegistered = false;
    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                    int state = intent.getIntExtra("state", -1);
                    switch (state) {
                        case 0:
                            Log.d(TAG, "Headset unplugged");
                            pause();
                            break;
                        case 1:
                            Log.d(TAG, "Headset plugged");
                            play();
                            break;
                    }
                }
            }
        }
    };

    priva