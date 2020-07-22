package com.maxfour.libreplayer.adapter.base;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialcab.MaterialCab;
import com.maxfour.libreplayer.R;
import com.maxfour.libreplayer.interfaces.CabHolder;

import java.util.ArrayList;


public abstract class AbsMultiSelectAdapter<VH extends RecyclerView.ViewHolder, I> extends RecyclerView.Adapter<VH> implements MaterialCab.Callback {
    @Nullable
    private final CabHolder cabHolder;
    private final Context context;
    private MaterialCab cab;
    private ArrayList<I> checked;
    private int menuRes;

    public AbsMultiS