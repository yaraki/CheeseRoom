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

package com.example.android.cheeseroom.repository

import androidx.lifecycle.LiveData
import androidx.room.Room
import android.content.Context
import com.example.android.cheeseroom.api.CheeseApi
import com.example.android.cheeseroom.db.CheeseDatabase
import com.example.android.cheeseroom.vo.Cheese
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CheeseRepository(
        private val database: CheeseDatabase,
        private val api: CheeseApi,
        private val executor: Executor) {

    companion object {

        @Volatile
        private var instance: CheeseRepository? = null

        fun getInstance(context: Context): CheeseRepository =
                instance ?: synchronized(this) {
                    instance ?: CheeseRepository(
                            TODO("The repository needs an instance of the database."),
                            CheeseApi(),
                            Executors.newSingleThreadExecutor()).also { instance = it }
                }

    }

    fun loadAllCheeses(): LiveData<List<Cheese>> {
        syncIfNecessary()
        return TODO("This should return all the cheeses in the database.")
    }

    fun findCheeseById(id: Long): LiveData<Cheese?> {
        return TODO("This method should return the cheese with the specified ID.")
    }

    fun updateCheese(cheese: Cheese) {
        executor.execute {
            TODO("The specified cheese should be updated in the database.")
        }
    }

    private fun syncIfNecessary() {
        executor.execute {
            TODO("If the local database is empty, fetch cheeses from the API and store them in the database.")
        }
    }

}
