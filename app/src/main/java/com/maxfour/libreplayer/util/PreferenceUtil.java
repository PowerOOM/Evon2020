
package com.maxfour.libreplayer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.maxfour.libreplayer.App;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.activities.MainActivity;
import com.maxfour.libreplayer.fragments.AlbumCoverStyle;
import com.maxfour.libreplayer.fragments.NowPlayingScreen;
import com.maxfour.libreplayer.fragments.mainactivity.folders.FoldersFragment;
import com.maxfour.libreplayer.helper.SortOrder;
import com.maxfour.libreplayer.helper.SortOrder.AlbumSongSortOrder;
import com.maxfour.libreplayer.model.CategoryInfo;
import com.maxfour.libreplayer.transform.CascadingPageTransformer;
import com.maxfour.libreplayer.transform.DepthTransformation;
import com.maxfour.libreplayer.transform.HingeTransformation;
import com.maxfour.libreplayer.transform.HorizontalFlipTransformation;
import com.maxfour.libreplayer.transform.NormalPageTransformer;
import com.maxfour.libreplayer.transform.VerticalFlipTransformation;
import com.maxfour.libreplayer.transform.VerticalStackTransformer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PreferenceUtil {
    public static final String LIBRARY_CATEGORIES = "library_categories";
    public static final String DESATURATED_COLOR = "desaturated_color";
    public static final String BLACK_THEME = "black_theme";
    public static final String DIALOG_CORNER = "dialog_corner";
    public static final String KEEP_SCREEN_ON = "keep_screen_on";
    public static final String TOGGLE_HOME_BANNER = "toggle_home_banner";
    public static final String NOW_PLAYING_SCREEN_ID = "now_playing_screen_id";
    public static final String CAROUSEL_EFFECT = "carousel_effect";
    public static final String COLORED_NOTIFICATION = "colored_notification";
    public static final String CLASSIC_NOTIFICATION = "classic_notification";
    public static final String GAPLESS_PLAYBACK = "gapless_playback";
    public static final String ALBUM_ART_ON_LOCKSCREEN = "album_art_on_lockscreen";
    public static final String BLURRED_ALBUM_ART = "blurred_album_art";
    public static final String NEW_BLUR_AMOUNT = "new_blur_amount";
    public static final String SLEEP_TIMER_FINISH_SONG = "sleep_timer_finish_song";
    public static final String TOGGLE_HEADSET = "toggle_headset";
    public static final String DOMINANT_COLOR = "dominant_color";
    public static final String GENERAL_THEME = "general_theme";
    public static final String CIRCULAR_ALBUM_ART = "circular_album_art";
    public static final String USER_NAME = "user_name";
    public static final String USER_BIO = "user_bio";
    public static final String TOGGLE_FULL_SCREEN = "toggle_full_screen";
    public static final String TOGGLE_VOLUME = "toggle_volume";
    public static final String TOGGLE_TAB_TITLES = "toggle_tab_titles";
    public static final String ROUND_CORNERS = "corner_window";
    public static final String TOGGLE_GENRE = "toggle_genre";
    public static final String PROFILE_IMAGE_PATH = "profile_image_path";
    public static final String BANNER_IMAGE_PATH = "banner_image_path";
    public static final String ADAPTIVE_COLOR_APP = "adaptive_color_app";
    public static final String TOGGLE_SEPARATE_LINE = "toggle_separate_line";
    public static final String ALBUM_GRID_STYLE = "album_grid_style";
    public static final String HOME_ARTIST_GRID_STYLE = "home_artist_grid_style";
    public static final String ARTIST_GRID_STYLE = "artist_grid_style";
    public static final String TOGGLE_ADD_CONTROLS = "toggle_add_controls";
    public static final String ALBUM_COVER_STYLE = "album_cover_style_id";
    public static final String ALBUM_COVER_TRANSFORM = "album_cover_transform";
    public static final String TAB_TEXT_MODE = "tab_text_mode";
    public static final String SAF_SDCARD_URI = "saf_sdcard_uri";
    private static final String GENRE_SORT_ORDER = "genre_sort_order";
    private static final String LAST_PAGE = "last_start_page";
    private static final String LAST_MUSIC_CHOOSER = "last_music_chooser";
    private static final String DEFAULT_START_PAGE = "default_start_page";
    private static final String INITIALIZED_BLACKLIST = "initialized_blacklist";
    private static final String ARTIST_SORT_ORDER = "artist_sort_order";
    private static final String ARTIST_SONG_SORT_ORDER = "artist_song_sort_order";
    private static final String ARTIST_ALBUM_SORT_ORDER = "artist_album_sort_order";
    private static final String ALBUM_SORT_ORDER = "album_sort_order";
    private static final String ALBUM_SONG_SORT_ORDER = "album_song_sort_order";
    private static final String SONG_SORT_ORDER = "song_sort_order";
    private static final String ALBUM_GRID_SIZE = "album_grid_size";
    private static final String ALBUM_GRID_SIZE_LAND = "album_grid_size_land";
    private static final String SONG_GRID_SIZE = "song_grid_size";
    private static final String SONG_GRID_SIZE_LAND = "song_grid_size_land";
    private static final String ARTIST_GRID_SIZE = "artist_grid_size";
    private static final String ARTIST_GRID_SIZE_LAND = "artist_grid_size_land";
    private static final String ALBUM_COLORED_FOOTERS = "album_colored_footers";
    private static final String SONG_COLORED_FOOTERS = "song_colored_footers";
    private static final String ARTIST_COLORED_FOOTERS = "artist_colored_footers";
    private static final String ALBUM_ARTIST_COLORED_FOOTERS = "album_artist_colored_footers";
    private static final String COLORED_APP_SHORTCUTS = "colored_app_shortcuts";
    private static final String AUDIO_DUCKING = "audio_ducking";
    private static final String LAST_ADDED_CUTOFF = "last_added_interval";
    private static final String LAST_SLEEP_TIMER_VALUE = "last_sleep_timer_value";
    private static final String NEXT_SLEEP_TIMER_ELAPSED_REALTIME = "next_sleep_timer_elapsed_real_time";
    private static final String IGNORE_MEDIA_STORE_ARTWORK = "ignore_media_store_artwork";
    private static final String LAST_CHANGELOG_VERSION = "last_changelog_version";
    private static final String INTRO_SHOWN = "intro_shown";
    private static final String AUTO_DOWNLOAD_IMAGES_POLICY = "auto_download_images_policy";
    private static final String START_DIRECTORY = "start_directory";
    private static final String SYNCHRONIZED_LYRICS_SHOW = "synchronized_lyrics_show";
    private static final String LOCK_SCREEN = "lock_screen";
    private static final String ALBUM_DETAIL_SONG_SORT_ORDER = "album_detail_song_sort_order";
    private static final String ARTIST_DETAIL_SONG_SORT_ORDER = "artist_detail_song_sort_order";
    private static final String LYRICS_OPTIONS = "lyrics_tab_position";
    private static final String CHOOSE_EQUALIZER = "choose_equalizer";
    private static final String TOGGLE_SHUFFLE = "toggle_shuffle";
    private static final String SONG_GRID_STYLE = "song_grid_style";
    private static final String TOGGLE_ANIMATIONS = "toggle_animations";
    private static final String LAST_KNOWN_LYRICS_TYPE = "LAST_KNOWN_LYRICS_TYPE";
    private static final String ALBUM_DETAIL_STYLE = "album_detail_style";
    private static final String PAUSE_ON_ZERO_VOLUME = "pause_on_zero_volume";
    private static final String NOW_PLAYING_SCREEN = "now_playing_screen";
    private static final String SNOW_FALL_EFFECT = "snow_fall_effect";
    private static final String FILTER_SONG = "filter_song";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtil(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isAllowedToDownloadMetadata(final Context context) {
        switch (getInstance(context).autoDownloadImagesPolicy()) {
            case "always":
                return true;
            case "only_wifi":
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                return netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.isConnectedOrConnecting();
            case "never":
            default:
                return false;
        }
    }

    @NonNull
    public static PreferenceUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(App.Companion.getContext());
        }
        return sInstance;
    }

    @StyleRes
    public static int getThemeResFromPrefValue(@NonNull String themePrefValue) {
        switch (themePrefValue) {
            case "light":
                return R.style.Theme_Player_Light;
            case "dark":
            default:
                return R.style.Theme_Player;
        }
    }

    public boolean desaturatedColor() {
        return mPreferences.getBoolean(DESATURATED_COLOR, false);
    }

    public void setDesaturatedColor(boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(DESATURATED_COLOR, value);
        editor.apply();
    }

    public boolean getSleepTimerFinishMusic() {
        return mPreferences.getBoolean(SLEEP_TIMER_FINISH_SONG, false);
    }

    public void setSleepTimerFinishMusic(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SLEEP_TIMER_FINISH_SONG, value);
        editor.apply();
    }

    public boolean isBlackMode() {
        return mPreferences.getBoolean(BLACK_THEME, false);
    }

    public String getUserBio() {
        return mPreferences.getString(USER_BIO, "");
    }

    public void setUserBio(String bio) {
        mPreferences.edit().putString(USER_BIO, bio).apply();
    }

    public int getFilterLength() {
        return mPreferences.getInt(FILTER_SONG, 20);
    }

    public float getDialogCorner() {
        return mPreferences.getInt(DIALOG_CORNER, 16);
    }

    public boolean isSnowFall() {
        return mPreferences.getBoolean(SNOW_FALL_EFFECT, false);
    }

    public final String getArtistSortOrder() {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z);
    }

    public void setArtistSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ARTIST_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getArtistSongSortOrder() {
        return mPreferences.getString(ARTIST_SONG_SORT_ORDER, SortOrder.ArtistSongSortOrder.SONG_A_Z);
    }

    public final boolean isHomeBanner() {
        return mPreferences.getBoolean(TOGGLE_HOME_BANNER, true);
    }

    public final String getArtistAlbumSortOrder() {
        return mPreferences.getString(ARTIST_ALBUM_SORT_ORDER, SortOrder.ArtistAlbumSortOrder.ALBUM_YEAR);
    }

    public final String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z);
    }

    public void setAlbumSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ALBUM_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getAlbumSongSortOrder() {
        return mPreferences
                .getString(ALBUM_SONG_SORT_ORDER, AlbumSongSortOrder.SONG_LIST);
    }

    public final String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z);
    }

    public void setSongSortOrder(final String sortOrder) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SONG_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public final String getGenreSortOrder() {
        return mPreferences.getString(GENRE_SORT_ORDER, SortOrder.GenreSortOrder.GENRE_A_Z);
    }

    public boolean isScreenOnEnabled() {
        return mPreferences.getBoolean(KEEP_SCREEN_ON, false);
    }

    public void setInitializedBlacklist() {
        final Editor editor = mPreferences.edit();
        editor.putBoolean(INITIALIZED_BLACKLIST, true);
        editor.apply();
    }

    public final boolean initializedBlacklist() {
        return mPreferences.getBoolean(INITIALIZED_BLACKLIST, false);
    }

    public boolean isExtraControls() {
        return mPreferences.getBoolean(TOGGLE_ADD_CONTROLS, false);
    }

    public boolean carouselEffect() {
        return mPreferences.getBoolean(CAROUSEL_EFFECT, false);
    }

    public boolean isRoundCorners() {
        return mPreferences.getBoolean(ROUND_CORNERS, false);
    }

    public void registerOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangedListener(
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    public final int getDefaultStartPage() {
        return Integer.parseInt(mPreferences.getString(DEFAULT_START_PAGE, "-1"));
    }

    public final int getLastPage() {
        return mPreferences.getInt(LAST_PAGE, R.id.action_song);
    }

    public void setLastPage(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_PAGE, value);
        editor.apply();
    }

    public void setLastLyricsType(int group) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_KNOWN_LYRICS_TYPE, group);
        editor.apply();
    }

    public final int getLastMusicChooser() {
        return mPreferences.getInt(LAST_MUSIC_CHOOSER, MainActivity.HOME);
    }

    public void setLastMusicChooser(int value) {
        mPreferences.edit().putInt(LAST_MUSIC_CHOOSER, value).apply();
    }

    public final boolean coloredNotification() {
        return mPreferences.getBoolean(COLORED_NOTIFICATION, true);
    }

    public final void setColoredNotification(boolean b) {
        mPreferences.edit().putBoolean(COLORED_NOTIFICATION, b).apply();
    }

    public final boolean classicNotification() {
        return mPreferences.getBoolean(CLASSIC_NOTIFICATION, false);
    }

    public void setClassicNotification(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(CLASSIC_NOTIFICATION, value);
        editor.apply();
    }

    public final NowPlayingScreen getNowPlayingScreen() {
        int id = mPreferences.getInt(NOW_PLAYING_SCREEN_ID, 0);
        for (NowPlayingScreen nowPlayingScreen : NowPlayingScreen.values()) {
            if (nowPlayingScreen.getId() == id) {
                return nowPlayingScreen;
            }
        }
        return NowPlayingScreen.ADAPTIVE;
    }

    @SuppressLint("CommitPrefEdits")
    public void setNowPlayingScreen(NowPlayingScreen nowPlayingScreen) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(NOW_PLAYING_SCREEN_ID, nowPlayingScreen.getId());
        editor.apply();
    }

    public final AlbumCoverStyle getAlbumCoverStyle() {
        int id = mPreferences.getInt(ALBUM_COVER_STYLE, 0);
        for (AlbumCoverStyle albumCoverStyle : AlbumCoverStyle.values()) {
            if (albumCoverStyle.getId() == id) {
                return albumCoverStyle;
            }
        }
        return AlbumCoverStyle.CARD;
    }

    public void setAlbumCoverStyle(AlbumCoverStyle albumCoverStyle) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(ALBUM_COVER_STYLE, albumCoverStyle.getId());
        editor.apply();
    }

    public void setColoredAppShortcuts(final boolean value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(COLORED_APP_SHORTCUTS, value);
        editor.apply();
    }

    public final boolean coloredAppShortcuts() {
        return mPreferences.getBoolean(COLORED_APP_SHORTCUTS, true);
    }

    public final boolean gaplessPlayback() {
        return mPreferences.getBoolean(GAPLESS_PLAYBACK, true);
    }

    public final boolean audioDucking() {
        return mPreferences.getBoolean(AUDIO_DUCKING, true);
    }

    public final boolean albumArtOnLockscreen() {
        return mPreferences.getBoolean(ALBUM_ART_ON_LOCKSCREEN, true);
    }

    public final boolean blurredAlbumArt() {
        return mPreferences.getBoolean(BLURRED_ALBUM_ART, false);
    }

    public final boolean ignoreMediaStoreArtwork() {
        return mPreferences.getBoolean(IGNORE_MEDIA_STORE_ARTWORK, false);
    }

    public int getLastSleepTimerValue() {
        return mPreferences.getInt(LAST_SLEEP_TIMER_VALUE, 30);
    }

    public void setLastSleepTimerValue(final int value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(LAST_SLEEP_TIMER_VALUE, value);
        editor.apply();
    }

    public long getNextSleepTimerElapsedRealTime() {
        return mPreferences.getLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, -1);
    }

    public void setNextSleepTimerElapsedRealtime(final long value) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(NEXT_SLEEP_TIMER_ELAPSED_REALTIME, value);
        editor.apply();
    }

    public final int getAlbumGridSize(Context context) {