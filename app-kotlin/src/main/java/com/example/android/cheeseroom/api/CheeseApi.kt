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

package com.example.android.cheeseroom.api

import androidx.annotation.WorkerThread
import com.example.android.cheeseroom.common.Cheeses
import com.example.android.cheeseroom.vo.Cheese


class CheeseApi {

    @WorkerThread
    fun fetchCheeses(): List<Cheese> {
        // This method only returns a list, but let's pretend that this is a network call.
        return Cheeses.CHEESES.mapIndexed { i, name -> Cheese((i + 1).toLong(), name, false) }
    }

}
