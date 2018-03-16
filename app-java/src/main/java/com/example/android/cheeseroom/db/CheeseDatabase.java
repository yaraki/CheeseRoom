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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.cheeseroom.vo.Cheese;

@Database(entities = {Cheese.class}, version = 1)
public abstract class CheeseDatabase extends RoomDatabase {

    private static CheeseDatabase sInstance;

    public static CheeseDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CheeseDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CheeseDatabase.class, "cheese.db").build();
                }
            }
        }
        return sInstance;
    }

    public abstract CheeseDao cheese();

}
