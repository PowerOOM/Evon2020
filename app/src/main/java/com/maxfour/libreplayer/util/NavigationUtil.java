package com.maxfour.libreplayer.util;

import static com.maxfour.libreplayer.util.PlayerUtil.openUrl;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.activities.AboutActivity;
import com.maxfour.libreplayer.activities.AlbumDetailsActivity;
import com.maxfour.libreplayer.activities.ArtistDetailActivity;
import com.maxfour.libreplayer.activities.GenreDetailsActivity;
import com.maxfour.libreplayer.activities.LyricsActivity;
import com.maxfour.libreplayer.activities.PlayingQueueActivity;
import com.maxfour.libreplayer.activities.PlaylistDetailActivity;
import com.maxfour.libreplayer.activities.SearchActivity;
import com.maxfour.libreplayer.activities.SettingsActivity;
import com.maxfour.libreplayer.activities.UserInfoActivity;
import com