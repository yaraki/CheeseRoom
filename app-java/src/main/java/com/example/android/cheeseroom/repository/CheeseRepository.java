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

package com.example.android.cheeseroom.repository;

import androidx.lifecycle.LiveData;
import android.content.Context;

import com.example.android.cheeseroom.api.CheeseApi;
import com.example.android.cheeseroom.db.CheeseDatabase;
import com.example.android.cheeseroom.vo.Cheese;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CheeseRepository {

    private static volatile CheeseRepository sInstance;

    public static CheeseRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CheeseRepository.class) {
                if (sInstance == null) {
                    sInstance = new CheeseRepository(
                            CheeseDatabase.getInstance(context),
                            new CheeseApi(),
                            Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    private CheeseDatabase mDatabase;
    private CheeseApi mApi;
    private Executor mExecutor;

    private CheeseRepository(CheeseDatabase database, CheeseApi api, Executor executor) {
        mDatabase = database;
        mApi = api;
        mExecutor = executor;
    }

    public LiveData<List<Cheese>> loadAllCheeses() {
        syncIfNecessary();
        return mDatabase.cheese().all();
    }

    public LiveData<Cheese> findCheeseById(long id) {
        return mDatabase.cheese().byId(id);
    }

    public void updateCheese(Cheese cheese) {
        mExecutor.execute(() -> mDatabase.cheese().update(cheese));
    }

    private void syncIfNecessary() {
        mExecutor.execute(() -> {
            if (mDatabase.cheese().count() == 0) {
                mDatabase.cheese().insertAll(mApi.fetchCheeses());
            }
        });
    }

}
