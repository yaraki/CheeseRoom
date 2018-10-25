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

import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.playground.vo.Message
import com.example.android.playground.vo.MessageImage
import com.example.android.playground.vo.User
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class PlaygroundDatabaseTest {

    private lateinit var database: PlaygroundDatabase

    @Before
    fun openDatabase() {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                PlaygroundDatabase::class.java).build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertMessage() {
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val userId = database.user().insert(User(0, "John"))
        val messageId = database.message().insert(Message(0, userId, "Hello", date))
        assertThat(messageId, `is`(1L))
        val message = database.message().byId(messageId)
        assertThat(message, `is`(not(nullValue())))
        message?.run {
            assertThat(id, `is`(1L))
            assertThat(content, `is`(equalTo("Hello")))
            assertThat(timestamp, `is`(equalTo(date)))
        }
    }

    @Test
    fun followUnfollow() {
        val a = User(1, "A")
        val b = User(2, "B")
        val user = database.user()
        user.insert(a)
        user.insert(b)
        assertThat(user.follows(a.id, b.id), `is`(false))
        user.follow(a.id, b.id)
        assertThat(user.follows(a.id, b.id), `is`(true))
        user.unfollow(a.id, b.id)
        assertThat(user.follows(a.id, b.id), `is`(false))
    }

    @Test
    fun followDuplicate() {
        val a = User(1, "A")
        val b = User(2, "B")
        database.user().run {
            insert(a)
            insert(b)
            follow(a, b)
            assertThat(countFollows(), `is`(1))
            follow(a, b)
            assertThat(countFollows(), `is`(1))
        }
    }

    @Test
    fun followers() {
        val a = User(1, "A")
        val b = User(2, "B")
        val c = User(3, "C")
        database.user().run {
            insert(a)
            insert(b)
            insert(c)
            assertThat(count(), `is`(3))
            follow(c, a)
            follow(b, a)
            assertThat(countFollows(), `is`(2))
            assertThat(countFollowers(a.id), `is`(2))
            assertThat(followers(a), allOf(hasItems(b, c), hasSize(2)))
        }
    }

    @Test
    fun userWithMessages() {
        val user = User(1, "u")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val a = Message(1, user.id, "a", date)
        val b = Message(2, user.id, "b", date)
        database.user().insert(user)
        database.message().insert(a)
        database.message().insert(b)
        val u = database.user().withMessages(1)
        assertThat(u, `is`(not(nullValue())))
        u?.run {
            assertThat(user.name, `is`(equalTo("u")))
            assertThat(messages, allOf(hasItems(a, b), hasSize(2)))
        }
    }

    @Test
    fun messageWithUser() {
        val user = User(1, "u")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val a = Message(1, user.id, "a", date)
        database.user().insert(user)
        database.message().insert(a)
        val messageWithUser = database.message().withUser(1)
        assertThat(messageWithUser, `is`(not(nullValue())))
        messageWithUser?.run {
            assertThat(content, `is`(equalTo("a")))
            assertThat(user.name, `is`(equalTo("u")))
        }
    }

    @Test
    fun timeline() {
        val follower = User(1, "follower")
        val followee = User(2, "followee")
        val stranger = User(3, "stranger")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val a = Message(1, followee.id, "a", date)
        val b = Message(2, follower.id, "b", date)
        val c = Message(3, stranger.id, "c", date)
        database.user().run {
            insert(follower)
            insert(followee)
            insert(stranger)
            follow(follower, followee)
        }
        database.message().run {
            insert(a)
            insert(b)
            insert(c)
        }
        assertThat(database.message().timeline(follower.id), allOf(hasSize(2), hasItems(a, b)))
    }

    @Test
    fun edit() {
        val user = User(1, "u")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val message = Message(1, user.id, "a", date)
        database.user().insert(user)
        database.message().insert(message)
        assertThat(database.message().byId(1)?.content, `is`(equalTo("a")))
        database.message().edit(1, "b")
        assertThat(database.message().byId(1)?.content, `is`(equalTo("b")))
    }

    @Test
    fun liveById() {
        val user = User(1, "u")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val message = Message(1, user.id, "a", date)

        // Prepare an observer.
        @Suppress("UNCHECKED_CAST")
        val observer = mock(Observer::class.java) as Observer<Message?>
        val liveData = database.message().liveById(1)

        // Observers should be registered in the main thread.
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            liveData.observeForever(observer)
        }

        // Insert the user and the message.
        database.user().insert(user)
        database.message().insert(message)

        // Verify that we get the value from the LiveData. Since the callback is asynchronous, we
        // should use timeout() and wait for the result to be delivered.
        verify(observer, timeout(3000).atLeastOnce()).onChanged(eq(message))

        // Observers should be unregistered in the main thread.
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun messageWithImages() {
        val user = User(1, "u")
        val date = GregorianCalendar(2006, 1 - 1, 2, 3, 4).time
        val message = Message(1, user.id, "a", date)
        val a = MessageImage(1, 1, "https://example.com/a.png")
        val b = MessageImage(2, 1, "https://example.com/b.png")
        database.user().insert(user)
        database.message().insert(message)
        database.message().insertImage(a)
        database.message().insertImage(b)
        val messageWithImages = database.message().byIdWithImages(1)
        assertThat(messageWithImages, `is`(notNullValue()))
        messageWithImages?.let { it ->
            assertThat(it.content, `is`(equalTo("a")))
            assertThat(it.images, hasSize(2))
        }
    }

}
