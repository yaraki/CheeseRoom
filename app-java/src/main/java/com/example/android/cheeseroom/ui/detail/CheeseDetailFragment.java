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

package com.example.android.cheeseroom.ui.detail;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.Fade;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cheeseroom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CheeseDetailFragment extends Fragment {

    private static final String ARG_CHEESE_ID = "cheese_id";

    private CheeseDetailViewModel mViewModel;

    private ImageView mImage;
    private TextView mName;
    private FloatingActionButton mFavorite;

    public static CheeseDetailFragment newInstance(long cheeseId) {
        final CheeseDetailFragment fragment = new CheeseDetailFragment();
        final Bundle args = new Bundle();
        args.putLong(ARG_CHEESE_ID, cheeseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new Fade());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        final long cheeseId = args != null ? args.getLong(ARG_CHEESE_ID) : 0;

        mViewModel = ViewModelProviders.of(this)
                .get(CheeseDetailViewModel.class);
        mViewModel.setCheeseId(cheeseId);
        mViewModel.getCheese().observe(this, cheese -> {
            if (cheese != null) {
                // The name
                mName.setText(cheese.name);
                // Show the image
                mImage.setImageResource(cheese.getDrawableRes());
                //
                mFavorite.setEnabled(true);
                if (cheese.favorite) {
                    mFavorite.setImageResource(R.drawable.ic_favorite);
                    mFavorite.setContentDescription(getString(R.string.cheese_favorite_true));
                } else {
                    mFavorite.setImageResource(R.drawable.ic_favorite_border);
                    mFavorite.setContentDescription(getString(R.string.cheese_favorite_mark));
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cheese_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mImage = view.findViewById(R.id.image);
        mName = view.findViewById(R.id.name);
        mFavorite = view.findViewById(R.id.favorite);
        mFavorite.setOnClickListener(v -> {
            mFavorite.setEnabled(false);
            mViewModel.toggleFavorite();
        });
    }

}
