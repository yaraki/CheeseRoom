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

package com.example.android.cheeseroom.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.example.android.cheeseroom.vo.Cheese

// TODO: This is a DAO.
interface CheeseDao {

    // TODO: This method should return all the cheeses as a LiveData.
    fun all(): LiveData<List<Cheese>>

    // TODO: This method should return the cheese specified by the ID as a LiveData.
    fun byId(id: Long): LiveData<Cheese?>

    // TODO: This method should return the number of cheeses.
    fun count(): Int

    // TODO: This method should insert the specified cheeses.
    fun insertAll(cheeses: List<Cheese>)

    // TODO: This method should update the specified cheese by its ID.
    fun update(cheese: Cheese)

}
