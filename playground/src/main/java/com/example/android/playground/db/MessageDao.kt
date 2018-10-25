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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.android.playground.vo.Message
import com.example.android.playground.vo.MessageImage
import com.example.android.playground.vo.MessageWithImages
import com.example.android.playground.vo.MessageWithUser
import com.example.android.playground.vo.User

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message): Long

    @Query("SELECT * FROM Message WHERE userId = :userId")
    fun byUserId(userId: Long): List<Message>

    @Delete
    fun delete(message: Message)

    @Delete
    fun deleteAll(messages: Array<Message>)

    @Delete
    fun deleteTogether(message: Message, user: User)

    @Query("DELETE FROM Message WHERE userId = :userId")
    fun deleteByUserId(userId: Long): Int

    @Query("SELECT * FROM Message")
    fun all(): List<Message>

    @Query("SELECT * FROM Message WHERE id = :id")
    fun byId(id: Long): Message?

    @Query("SELECT * FROM Message WHERE id = :id")
    fun liveById(id: Long): LiveData<Message?>

    // This demonstrates use of a POJO as return type of a DAO method.
    @Query("""
        SELECT Message.id
             , Message.content
             , Message.timestamp
             , User.id AS user_id
             , User.name AS user_name
        FROM Message INNER JOIN User ON Message.userId = User.id
        WHERE Message.id = :id
        """)
    fun withUser(id: Long): MessageWithUser?

    /**
     * Returns the list of [Message] from all the users followed by the user specified by [userId].
     */
    @Query("""
        SELECT * FROM Message
        WHERE userId IN (SELECT followeeId FROM Follow WHERE followerId = :userId)
           OR userId = :userId
        ORDER BY timestamp DESC
           """)
    fun timeline(userId: Long): List<Message>

    @Query("UPDATE Message SET content = :content WHERE id = :id")
    fun edit(id: Long, content: String)

    // This demonstrates use of @Relation in POJOs.
    @Transaction
    @Query("SELECT * FROM Message WHERE id = :id")
    fun byIdWithImages(id: Long): MessageWithImages?

    @Insert
    fun insertImage(image: MessageImage)

}
