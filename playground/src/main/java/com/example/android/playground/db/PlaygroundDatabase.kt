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

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.android.playground.vo.Cheese
import com.example.android.playground.vo.Follow
import com.example.android.playground.vo.Message
import com.example.android.playground.vo.MessageImage
import com.example.android.playground.vo.User

@Database(
        entities = [
            User::class,
            Follow::class,
            Message::class,
            MessageImage::class,
            Cheese::class
        ],
        version = 3)
@TypeConverters(DateConverter::class)
abstract class PlaygroundDatabase : RoomDatabase() {

    companion object {

        // This is a simple migration that just adds a new table. You can copy and paste the SQL
        // from the schema file Room exports (2.json).
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `Cheese` (
                        `id` INTEGER NOT NULL, `name` TEXT NOT NULL
                      , `favorite` INTEGER NOT NULL
                      , PRIMARY KEY(`id`))
                """)
            }
        }

        // This is a more complex migration including conversion of existing data.
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Rename the old table.
                database.execSQL("ALTER TABLE `Cheese` RENAME TO `old_Cheese`")
                // Create a new table with the new schema.
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `Cheese` (
                        `id` INTEGER NOT NULL
                      , `name` TEXT NOT NULL
                      , `score` INTEGER NOT NULL
                      , PRIMARY KEY(`id`))
                """)
                // Convert existing data to the new format.
                database.query("SELECT id, name, favorite FROM old_Cheese").let { c ->
                    val indexId = c.getColumnIndexOrThrow("id")
                    val indexName = c.getColumnIndexOrThrow("name")
                    val indexFavorite = c.getColumnIndexOrThrow("favorite")
                    val values = ContentValues()
                    while (c.moveToNext()) {
                        values.put("id", c.getLong(indexId))
                        values.put("name", c.getString(indexName))
                        val favorite = c.getInt(indexFavorite) != 0
                        values.put("score", if (favorite) 5 else 0)
                        database.insert("Cheese", SQLiteDatabase.CONFLICT_REPLACE, values)
                    }
                    c.close()
                }
                // Drop the old table.
                database.execSQL("DROP TABLE `old_Cheese`")
            }
        }

        @Volatile
        private var instance: PlaygroundDatabase? = null

        @Suppress("unused")
        fun getInstance(context: Context): PlaygroundDatabase =
                instance ?: synchronized(this) {
                    instance ?: Room
                            .databaseBuilder(context, PlaygroundDatabase::class.java, "pg.db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .build()
                            .also { instance = it }
                }

    }

    abstract fun user(): UserDao
    abstract fun message(): MessageDao

}
