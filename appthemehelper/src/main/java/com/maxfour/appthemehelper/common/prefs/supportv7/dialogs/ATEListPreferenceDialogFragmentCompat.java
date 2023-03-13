package com.maxfour.appthemehelper.common.prefs.supportv7.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.maxfour.appthemehelper.common.prefs.supportv7.ATEListPreference;

public class ATEListPreferenceDialogFragmentCompat extends ATEPreferenceDialogFragment {
    private int mClickedDialogEntryIndex;

    @NonNull
    public static ATEListPreferenceDialogFragmentCompat newInstance(@NonNull String key) {
        final ATEListPreferenceDialogFragmentCompat fragment = new ATEListPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

   