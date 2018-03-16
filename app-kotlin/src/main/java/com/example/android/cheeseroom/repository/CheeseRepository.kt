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

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
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
                            Room.databaseBuilder(context, CheeseDatabase::class.java, "cheese.db")
                                    .build(),
                            CheeseApi(),
                            Executors.newSingleThreadExecutor()).also { instance = it }
                }

    }

    fun loadAllCheeses(): LiveData<List<Cheese>> {
        syncIfNecessary()
        return database.cheese().all()
    }

    fun findCheeseById(id: Long): LiveData<Cheese?> {
        return database.cheese().byId(id)
    }

    fun updateCheese(cheese: Cheese) {
        executor.execute {
            database.cheese().update(cheese)
        }
    }

    private fun syncIfNecessary() {
        executor.execute {
            if (database.cheese().count() == 0) {
                database.cheese().insertAll(api.fetchCheeses())
            }
        }
    }

}
