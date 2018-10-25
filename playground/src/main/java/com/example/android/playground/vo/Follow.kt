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
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(entity = User::class, childColumns = ["followerId"], parentColumns = ["id"]),
            ForeignKey(entity = User::class, childColumns = ["followeeId"], parentColumns = ["id"])
        ],
        indices = [
            Index(value = ["followerId", "followeeId"], unique = true)
        ]
)
data class Follow(
        @PrimaryKey(autoGenerate = true)
        val id: Long,

        val followerId: Long,

        val followeeId: Long
)
