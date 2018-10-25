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

package com.example.android.cheeseroom.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.annotation.NonNull;

import com.example.android.cheeseroom.vo.Cheese;

import java.util.List;

@Dao
public interface CheeseDao {

    @Query("SELECT * FROM Cheese")
    LiveData<List<Cheese>> all();

    @Query("SELECT * FROM Cheese WHERE id = :id")
    LiveData<Cheese> byId(long id);

    @Query("SELECT COUNT(*) FROM Cheese")
    int count();

    @Insert
    void insertAll(List<Cheese> cheeses);

    @Update
    void update(Cheese cheese);

}
