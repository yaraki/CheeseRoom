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

package com.example.android.cheeseroom.vo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.example.android.cheeseroom.common.Cheeses;


@Entity
public class Cheese {

    @PrimaryKey
    public final long id;

    @NonNull
    public final String name;

    public final boolean favorite;

    public Cheese(long id, @NonNull String name, boolean favorite) {
        this.id = id;
        this.name = name;
        this.favorite = favorite;
    }

    @DrawableRes
    public int getDrawableRes() {
        return Cheeses.getDrawableForCheese(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cheese cheese = (Cheese) o;
        return id == cheese.id && favorite == cheese.favorite && name.equals(cheese.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + (favorite ? 1 : 0);
        return result;
    }

}
