/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.cheeseroom.ui.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.core.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cheeseroom.R;
import com.example.android.cheeseroom.vo.Cheese;

import java.util.ArrayList;
import java.util.List;


class CheeseListAdapter extends RecyclerView.Adapter<CheeseListAdapter.CheeseViewHolder> {

    interface OnCheeseSelectedListener {
        void onCheeseSelected(long cheeseId);
    }

    @NonNull
    private final OnCheeseSelectedListener mListener;

    private final ArrayList<Cheese> mCheeses = new ArrayList<>();

    private final Drawable mFavoriteDrawable;

    CheeseListAdapter(@NonNull Context context, @NonNull OnCheeseSelectedListener listener) {
        mListener = listener;
        mFavoriteDrawable = VectorDrawableCompat.create(context.getResources(),
                R.drawable.ic_favorite, context.getTheme());
        if (mFavoriteDrawable != null) {
            DrawableCompat.setTint(mFavoriteDrawable, Color.WHITE);
        }
    }

    void setCheeses(List<Cheese> cheeses) {
        mCheeses.clear();
        mCheeses.addAll(cheeses);
        notifyDataSetChanged();
    }

    @Override
    public CheeseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheeseViewHolder(parent, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(CheeseViewHolder holder, int position) {
        holder.bind(mCheeses.get(position), mFavoriteDrawable);
    }

    @Override
    public int getItemCount() {
        return mCheeses.size();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mListener.onCheeseSelected((Long) view.getTag(R.id.cheese_id));
        }

    };

    static class CheeseViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        CheeseViewHolder(ViewGroup parent, View.OnClickListener onClickListener) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cheese_list_item, parent, false));
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(onClickListener);
            ViewGroupCompat.setTransitionGroup((ViewGroup) itemView, true);
        }

        void bind(Cheese cheese, Drawable favoriteDrawable) {
            itemView.setTag(R.id.cheese_id, cheese.id);
            image.setImageResource(cheese.getDrawableRes());
            name.setText(cheese.name);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(name,
                    null, null, cheese.favorite ? favoriteDrawable : null, null);
        }

    }

}
