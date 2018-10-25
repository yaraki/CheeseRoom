/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.example.android.playground.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

// This table is added in version 2
@Entity
data class Cheese(

        @PrimaryKey
        val id: Long,

        val name: String,

        // This field replaced "val favorite: Boolean" in version 3.
        val score: Int)
