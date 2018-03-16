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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.android.cheeseroom.repository.CheeseRepository;
import com.example.android.cheeseroom.vo.Cheese;


public class CheeseDetailViewModel extends AndroidViewModel {

    private final CheeseRepository mRepository;

    // The ID of the cheese to show; this is an input from the UI
    private MutableLiveData<Long> mCheeseId = new MutableLiveData<>();

    private LiveData<Cheese> mCheese;

    public CheeseDetailViewModel(@NonNull Application application) {
        super(application);

        mRepository = CheeseRepository.getInstance(application);
        mCheese = Transformations.switchMap(mCheeseId, mRepository::findCheeseById);
    }

    void setCheeseId(final long cheeseId) {
        mCheeseId.setValue(cheeseId);
    }

    LiveData<Cheese> getCheese() {
        return mCheese;
    }

    void toggleFavorite() {
        final Cheese cheese = mCheese.getValue();
        if (cheese == null) {
            return;
        }
        mRepository.updateCheese(new Cheese(cheese.id, cheese.name, !cheese.favorite));
    }

}
