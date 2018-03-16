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

import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.support.test.InstrumentationRegistry
import android.support.test.filters.MediumTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MigrationTest {

    companion object {
        const val DATABASE_NAME = "migration-test.db"
    }

    //
    @Rule
    @JvmField
    val helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
            PlaygroundDatabase::class.java.canonicalName)

    @Test
    fun migrate1To2() {
        // Create version 1. The schema is read from 1.json
        val db1 = helper.createDatabase(DATABASE_NAME, 1)
        db1.close()
        // Run migration to version 2 and validate its schema with 2.json.
        val db2 = helper.runMigrationsAndValidate(DATABASE_NAME, 2, true,
                PlaygroundDatabase.MIGRATION_1_2)
        db2.close()
    }

    @Test
    fun migrate2To3() {
        // Create version 2.
        helper.createDatabase(DATABASE_NAME, 2).use { db2 ->
            // Prepare some values in the version 2 schema. You cannot use DAO here because it is
            // bound to the latest version of the database.
            db2.insert("Cheese", 0, ContentValues().apply {
                put("id", 1L)
                put("name", "a")
                put("favorite", true)
            })
            db2.insert("Cheese", 0, ContentValues().apply {
                put("id", 2L)
                put("name", "b")
                put("favorite", false)
            })
        }
        // Run migration to version 3 and validate its schema with 3.json.
        helper.runMigrationsAndValidate(DATABASE_NAME, 3, true,
                PlaygroundDatabase.MIGRATION_2_3).use { db3 ->
            // Test the result of data conversion.
            db3.query("SELECT * FROM Cheese ORDER BY id ASC").let { c ->
                // Again, you cannot use DAO here. 3 is the latest version now, but the schema
                // might change in the future, and this test should keep on running after that.
                val indexId = c.getColumnIndexOrThrow("id")
                val indexName = c.getColumnIndexOrThrow("name")
                val indexScore = c.getColumnIndexOrThrow("score")
                assertThat(c.count, `is`(2))
                // Cheese 1: a
                assertThat(c.moveToNext(), `is`(true))
                assertThat(c.getLong(indexId), `is`(1L))
                assertThat(c.getString(indexName), `is`(equalTo("a")))
                assertThat(c.getInt(indexScore), `is`(5))
                // Cheese 2: b
                assertThat(c.moveToNext(), `is`(true))
                assertThat(c.getLong(indexId), `is`(2L))
                assertThat(c.getString(indexName), `is`(equalTo("b")))
                assertThat(c.getInt(indexScore), `is`(0))
                // Off-topic, but you cannot use Kotlin 'use' function for Cursor if you are
                // targeting API Levels below 16 because Cursor didn't implement Closeable back
                // then.
                c.close()
            }
        }
    }

}
