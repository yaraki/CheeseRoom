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

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.cheeseroom.R;


public class CheeseListFragment extends Fragment {

    public interface Listener {
        void onCheeseSelected(long cheeseId);
    }

    private CheeseListAdapter mAdapter;

    public static CheeseListFragment newInstance() {
        return new CheeseListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Slide slide = new Slide(GravityCompat.START);
        slide.setDuration(150);
        setExitTransition(slide);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final CheeseListViewModel viewModel = ViewModelProviders.of(this)
                .get(CheeseListViewModel.class);
        viewModel.getCheeses().observe(this, cheeses -> {
            if (cheeses != null) {
                mAdapter.setCheeses(cheeses);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cheese_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView list = view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(list.getContext(), 2));
        mAdapter = new CheeseListAdapter(view.getContext(), (cheeseId) -> {
            final Activity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                ((Listener) activity).onCheeseSelected(cheeseId);
            }
        });
        list.setAdapter(mAdapter);
    }

}
