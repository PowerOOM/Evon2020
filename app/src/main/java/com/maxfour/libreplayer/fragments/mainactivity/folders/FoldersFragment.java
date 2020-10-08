package com.maxfour.libreplayer.fragments.mainactivity.folders;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.maxfour.appthemehelper.ThemeStore;
import com.maxfour.appthemehelper.common.ATHToolbarActivity;
import com.maxfour.appthemehelper.util.ATHUtil;
import com.maxfour.appthemehelper.util.ToolbarContentTintHelper;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.adapter.SongFileAdapter;
import com.maxfour.libreplayer.dialogs.OptionsSheetDialogFragment;
import com.maxfour.libreplayer.fragments.base.AbsMainActivityFragment;
import com.maxfour.libreplayer.helper.MusicPlayerRemote;
import com.maxfour.libreplayer.helper.menu.SongMenuHelper;
import com.maxfour.libreplayer.helper.menu.SongsMenuHelper;
import com.maxfour.libreplayer.interfaces.CabHolder;
import com.maxfour.libreplayer.interfaces.LoaderIds;
import com.maxfour.libreplayer.interfaces.MainActivityFragmentCallbacks;
import com.maxfour.libreplayer.misc.DialogAsyncTask;
import com.maxfour.libreplayer.misc.UpdateToastMediaScannerCompletionListener;
import com.maxfour.libreplayer.misc.WrappedAsyncTaskLoader;
import com.maxfour.libreplayer.model.Song;
import com.maxfour.libreplayer.util.DensityUtil;
import com.maxfour.libreplayer.util.FileUtil;
import com.maxfour.libreplayer.util.NavigationUtil;
import com.maxfour.libreplayer.util.PlayerColorUtil;
import com.maxfour.libreplayer.util.PreferenceUtil;
import com.maxfour.libreplayer.util.ViewUtil;
import com.maxfour.libreplayer.views.BreadCrumbLayout;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FoldersFragment extends AbsMainActivityFragment implements
        MainActivityFragmentCallbacks,
        CabHolder, BreadCrumbLayout.SelectionCallback, SongFileAdapter.Callbacks,
        AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<List<File>> {

    public static final String TAG = FoldersFragment.class.getSimpleName();
    public static final FileFilter AUDIO_FILE_FILTER = file -> !file.isHidden() && (file.isDirectory() ||
            FileUtil.fileIsMimeType(file, "audio/*", MimeTypeMap.getSingleton()) ||
            FileUtil.fileIsMimeType(file, "application/opus", MimeTypeMap.getSingleton()) ||
            FileUtil.fileIsMimeType(file, "application/ogg", MimeTypeMap.getSingleton()));

    private static final String PATH = "path";
    private static final String CRUMBS = "crumbs";
    private static final int LOADER_ID = LoaderIds.Companion.getFOLDERS_FRAGMENT();

    private View coordinatorLayout, empty;
    private TextView emojiText;
    private MaterialCardView toolbarContainer;
    private Toolbar toolbar;
    private BreadCrumbLayout breadCrumbs;
    private AppBarLayout appBarLayout;
    private FastScrollRecyclerView recyclerView;

    private Comparator<File> fileComparator = (lhs, rhs) -> {
        if (lhs.isDirectory() && !rhs.isDirectory()) {
            return -1;
        } else if (!lhs.isDirectory() && rhs.isDirectory()) {
            return 1;
        } else {
            return lhs.getName().compareToIgnoreCase
                    (rhs.getName());
        }
    };
    private SongFileAdapter adapter;
    private MaterialCab cab;

    public FoldersFragment() {
    }

    public static FoldersFragment newInstance(Context context) {
        return newInstance(PreferenceUtil.getInstance(context).getStartDirectory());
    }

    public static FoldersFragment newInstance(File directory) {
        FoldersFragment frag = new FoldersFragment();
        Bundle b = new Bundle();
        b.putSerializable(PATH, directory);
        frag.setArguments(b);
        return frag;
    }

    public static File getDefaultStartDirectory() {
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File startFolder;
        if (musicDir.exists() && musicDir.isDirectory()) {
            startFolder = musicDir;
        } else {
            File externalStorage = Environment.getExternalStorageDirectory();
            if (externalStorage.exists() && externalStorage.isDirectory()) {
                startFolder = externalStorage;
            } else {
                startFolder = new File("/"); // root
            }
        }
        return startFolder;
    }

    private static File tryGetCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private void initViews(View view) {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        toolbarContainer = view.findViewById(R.id.toolbarContainer);
        recyclerView = view.findViewById(R.id.recyclerView);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        breadCrumbs = view.findViewById(R.id.breadCrumbs);
        toolbar = view.findViewById(R.id.toolbar);
        empty = view.findViewById(android.R.id.empty);
        emojiText = view.findViewById(R.id.emptyEmoji);
    }

    private void setCrumb(BreadCrumbLayout.Crumb crumb, boolean addToHistory) {
        if (crumb == null) {
            return;
        }
        saveScrollPosition();
        breadCrumbs.setActiveOrAdd(crumb, false);
        if (addToHistory) {
            breadCrumbs.addHistory(crumb);
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void saveScrollPosition() {
        BreadCrumbLayout.Crumb crumb = getActiveCrumb();
        if (crumb != null) {
            crumb.setScrollPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        }
    }

    @Nullable
    private BreadCrumbLayout.Crumb getActiveCrumb() {
        return breadCrumbs != null && breadCrumbs.size() > 0 ? breadCrumbs
                .getCrumb(breadCrumbs.getActiveIndex()) : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (breadCrumbs != null) {
            outState.putParcelable(CRUMBS, breadCrumbs.getStateWrapper());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.o