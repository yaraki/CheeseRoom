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

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.example.android.playground.vo.Author
import com.example.android.playground.vo.AuthorDetail
import com.example.android.playground.vo.Book
import com.example.android.playground.vo.BookDetail

@Dao
interface BookDao {

    @Insert
    fun insertAuthors(vararg authors: Author)

    @Insert
    fun insertBooks(vararg books: Book)

    @Query("SELECT * FROM Author")
    fun allAuthors(): List<Author>

    @Query("SELECT * FROM Book")
    fun allBooks(): List<Book>

    @Query("""
        SELECT
            Book.id
          , Book.title
          , Author.id AS author_id
          , Author.name AS author_name
        FROM Book INNER JOIN Author ON Book.authorId = Author.id
    """)
    fun allBookDetails(): List<BookDetail>

    @Transaction
    @Query("SELECT * FROM Author")
    fun allAuthorDetails(): List<AuthorDetail>

    @Query("SELECT * FROM Author")
    fun liveAllAuthors(): LiveData<List<Author>>

}
