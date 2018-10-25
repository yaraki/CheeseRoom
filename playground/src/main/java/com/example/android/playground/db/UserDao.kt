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

package com.example.android.playground.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.android.playground.vo.Follow
import com.example.android.playground.vo.User
import com.example.android.playground.vo.UserWithMessages

@Dao
abstract class UserDao {

    @Insert
    abstract fun insert(user: User): Long

    @Query("SELECT COUNT(*) FROM User")
    abstract fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertFollow(follow: Follow): Long

    @Query("SELECT id FROM Follow WHERE followerId = :followerId AND followeeId = :followeeId")
    abstract fun findFollow(followerId: Long, followeeId: Long): Long?

    @Query("DELETE FROM Follow WHERE followerId = :followerId AND followeeId = :followeeId")
    abstract fun deleteFollow(followerId: Long, followeeId: Long): Int

    @Query("SELECT COUNT(*) FROM Follow")
    abstract fun countFollows(): Int

    fun follow(followerId: Long, followeeId: Long) = insertFollow(Follow(0, followerId, followeeId))

    fun follow(follower: User, followee: User) = follow(follower.id, followee.id)

    fun follows(followerId: Long, followeeId: Long) = findFollow(followerId, followeeId) != null

    fun unfollow(followerId: Long, followeeId: Long) = deleteFollow(followerId, followeeId) > 0

    @Query("SELECT * FROM User WHERE id IN (SELECT followerId FROM Follow WHERE followeeId = :userId)")
    abstract fun followers(userId: Long): List<User>

    fun followers(user: User) = followers(user.id)

    @Query("SELECT COUNT(*) FROM Follow WHERE followeeId = :userId")
    abstract fun countFollowers(userId: Long): Int

    // This demonstrates use of @Embedded and @Relation in POJOs. Since this will be two SELECT
    // queries for User and Message tables, you should wrap it in @Transaction for consistency
    // between them.
    @Transaction
    @Query("SELECT * FROM User WHERE id = :id")
    abstract fun withMessages(id: Long): UserWithMessages?

}
