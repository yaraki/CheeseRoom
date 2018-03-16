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

package com.example.android.cheeseroom.db

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.example.android.cheeseroom.vo.Cheese
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class CheeseDatabaseTest {

    companion object {
        private fun <T> consistsOf(vararg items: T) = ArgumentMatcher<List<T>> { list ->
            items.size == list.size && items.all { list.contains(it) }
        }

        private fun db() = timeout(3000L).atLeastOnce()
    }

    private lateinit var database: CheeseDatabase

    @Before
    fun openDatabase() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                CheeseDatabase::class.java).build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    @SmallTest
    fun insertAndUpdate() {
        // The table is empty
        assertThat(database.cheese().count(), `is`(0))

        // Set up observers so we can check the results below
        val all = database.cheese().all()
        val cheese2 = database.cheese().byId(2)
        @Suppress("UNCHECKED_CAST")
        val allObserver = mock(Observer::class.java) as Observer<List<Cheese>>
        @Suppress("UNCHECKED_CAST")
        val cheese2Observer = mock(Observer::class.java) as Observer<Cheese?>
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            all.observeForever(allObserver)
            cheese2.observeForever(cheese2Observer)
        }

        // Insert an item
        database.cheese().insertAll(listOf(Cheese(1, "first", false)))
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                Cheese(1, "first", false))))
        verify(cheese2Observer, never()).onChanged(any(Cheese::class.java))
        assertThat(database.cheese().count(), `is`(1))

        // Insert another item
        database.cheese().insertAll(listOf(Cheese(2, "second", true)))
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                Cheese(1, "first", false),
                Cheese(2, "second", true))))
        verify(cheese2Observer, db()).onChanged(eq(Cheese(2, "second", true)))
        assertThat(database.cheese().count(), `is`(2))

        // Modify the second item
        database.cheese().update(Cheese(2, "second", false))
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                Cheese(1, "first", false),
                Cheese(2, "second", false))))
        verify(cheese2Observer, db()).onChanged(eq(Cheese(2, "second", false)))
        assertThat(database.cheese().count(), `is`(2))

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            all.removeObserver(allObserver)
            cheese2.removeObserver(cheese2Observer)
        }
    }

}
