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

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteConstraintException
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.example.android.playground.vo.Author
import com.example.android.playground.vo.Book
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class BookDaoTest {

    companion object {
        private fun <T> consistsOf(vararg items: T) = ArgumentMatcher<List<T>> { list ->
            items.size == list.size && items.all { list.contains(it) }
        }
    }

    private lateinit var database: PlaygroundDatabase
    private lateinit var dao: BookDao

    @Before
    fun openDatabase() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                PlaygroundDatabase::class.java).build()
        dao = database.book()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAuthorAndBook() {
        dao.insertAuthors(Author(1, "Akutagawa"))
        dao.insertBooks(Book(1, "Kappa", 1))
        dao.allBooks().let { books ->
            assertThat(books, hasSize(1))
            assertThat(books, hasItems(Book(1, "Kappa", 1)))
        }
        dao.allAuthors().let { authors ->
            assertThat(authors, hasSize(1))
            assertThat(authors, hasItems(Author(1, "Akutagawa")))
        }
    }

    @Test
    fun insertBookWithoutAuthor() {
        try {
            dao.insertBooks(Book(1, "Kappa", 1))
            fail("Was expecting an exception")
        } catch (e: SQLiteConstraintException) {
            assertThat(e.message, containsString("FOREIGN KEY"))
        }
    }

    @Test
    fun testBookDetail() {
        dao.insertAuthors(Author(1, "Akutagawa"))
        dao.insertBooks(Book(1, "Kappa", 1))
        dao.allBookDetails().let { details ->
            assertThat(details, hasSize(1))
            details.first().let { detail ->
                assertThat(detail.title, `is`(equalTo("Kappa")))
                assertThat(detail.author.name, `is`(equalTo("Akutagawa")))
            }
        }
    }

    @Test
    fun testAuthorDetail() {
        dao.insertAuthors(Author(1, "Akutagawa"))
        dao.insertBooks(Book(1, "Kappa", 1))
        dao.insertBooks(Book(2, "Imogayu", 1))
        dao.allAuthorDetails().let { details ->
            assertThat(details, hasSize(1))
            details.first().let { detail ->
                assertThat(detail.author.name, `is`(equalTo("Akutagawa")))
                assertThat(detail.books, hasSize(2))
                assertThat(detail.books, hasItems(Book(1, "Kappa", 1), Book(2, "Imogayu", 1)))
            }
        }
    }

    @Test
    fun testLiveData() {
        val authors = dao.liveAllAuthors()
        @Suppress("UNCHECKED_CAST")
        val observer = mock(Observer::class.java) as Observer<List<Author>>
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            authors.observeForever(observer)
        }
        dao.insertAuthors(Author(1, "Akutagawa"))
        verify(observer, timeout(500).atLeastOnce())
                .onChanged(argThat(consistsOf(Author(1, "Akutagawa"))))
        dao.insertAuthors(Author(2, "Dazai"))
        verify(observer, timeout(500).atLeastOnce())
                .onChanged(argThat(consistsOf(Author(1, "Akutagawa"), Author(2, "Dazai"))))
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            authors.removeObserver(observer)
        }
    }

}
