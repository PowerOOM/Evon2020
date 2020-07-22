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

    public AbsMultiSelectAdapter(@NonNull Context context, @Nullable CabHolder cabHolder, @MenuRes int menuRes) {
        this.cabHolder = cabHolder;
        checked = new ArrayList<>();
        this.menuRes = menuRes;
        this.context = context;
    }

    protected void setMultiSelectMenuRes(@MenuRes int menuRes) {
        this.menuRes = menuRes;
    }

    protected boolean toggleChecked(final int position) {
        if (cabHolder != null) {
            I identifier = getIdentifier(position);
            if (identifier == null) return false;

            if (!checked.remove(identifier)) checked.add(identifier);

            notifyItemChanged(position);
            updateCab();
            return true;
        }
        return false;
    }

    protected void checkAll() {
        if (cabHolder != null) {
            checked.clear();
            for (int i = 0; i < getItemCount(); i++) {
                I identifier = getIdentifier(i);
                if (identifier != null) {
                    checked.add(identifier);
                }
            }
            notifyDataSetChanged();
            updateCab();
        }
    }

    private void updateCab() {
        if (cabHolder != null) {
            if (cab == null || !c