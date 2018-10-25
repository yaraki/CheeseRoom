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

package com.example.android.cheeseroom.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.cheeseroom.repository.CheeseRepository
import com.example.android.cheeseroom.vo.Cheese


class CheeseDetailViewModel(application: Application) : AndroidViewModel(application) {

    /** Data source */
    private val repository = CheeseRepository.getInstance(application)

    /** The ID of the cheese to show; this is an input from the UI */
    private val cheeseId = MutableLiveData<Long>()

    val cheese: LiveData<Cheese?> = Transformations.switchMap(cheeseId) { cheeseId ->
        repository.findCheeseById(cheeseId)
    }

    fun setCheeseId(cheeseId: Long) {
        this.cheeseId.value = cheeseId
    }

    fun toggleFavorite() {
        this.cheese.value?.let {
            repository.updateCheese(Cheese(it.id, it.name, !it.favorite))
        }
    }

}
