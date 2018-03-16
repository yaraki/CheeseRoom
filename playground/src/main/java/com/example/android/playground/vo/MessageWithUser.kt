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

import android.arch.persistence.room.Embedded
import java.util.*

/**
 * This demonstrates use of [Embedded] in a POJO. This is useful for receiving the return values
 * from INNER JOIN and so on.
 *
 * Alternatively, you can avoid repeating the column names and types by embedding the main [Message]
 * entity as well. See [UserWithMessages].
 */
data class MessageWithUser(
        val id: Long,
        val content: String,
        val timestamp: Date,
        @Embedded(prefix = "user_")
        val user: User
)
