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

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.example.android.cheeseroom.repository.CheeseRepository;
import com.example.android.cheeseroom.vo.Cheese;

import java.util.List;


public class CheeseListViewModel extends AndroidViewModel {

    private final LiveData<List<Cheese>> mCheeses;

    public CheeseListViewModel(@NonNull Application application) {
        super(application);

        final CheeseRepository repository = CheeseRepository.getInstance(application);
        mCheeses = repository.loadAllCheeses();
    }

    LiveData<List<Cheese>> getCheeses() {
        return mCheeses;
    }

}
