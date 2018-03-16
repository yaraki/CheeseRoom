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

package com.example.android.cheeseroom.api;

import android.support.annotation.WorkerThread;

import com.example.android.cheeseroom.common.Cheeses;
import com.example.android.cheeseroom.vo.Cheese;

import java.util.ArrayList;
import java.util.List;

public class CheeseApi {

    @WorkerThread
    public List<Cheese> fetchCheeses() {
        // This method only returns a list, but let's pretend that this is a network call.
        final ArrayList<Cheese> cheeses = new ArrayList<>();
        for (int i = 0; i < Cheeses.CHEESES.length; i++) {
            cheeses.add(new Cheese(i + 1, Cheeses.CHEESES[i], false));
        }
        return cheeses;
    }

}
