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

package com.example.android.cheeseroom.db;

import com.example.android.cheeseroom.vo.Cheese;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@RunWith(AndroidJUnit4.class)
public class CheeseDatabaseTest {

    private CheeseDatabase mDatabase;

    @Before
    public void openDatabase() {
        mDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                CheeseDatabase.class).build();
    }

    @After
    public void closeDatabase() {
        mDatabase.close();
    }

    @SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "unchecked"})
    @Test
    @SmallTest
    public void insertAndUpdate() {
        // The table is empty
        assertThat(mDatabase.cheese().count(), is(0));

        // Set up observers so we can check the results below
        final LiveData<List<Cheese>> all = mDatabase.cheese().all();
        final LiveData<Cheese> cheese2 = mDatabase.cheese().byId(2);
        final Observer<List<Cheese>> allObserver = mock(Observer.class);
        final Observer<Cheese> cheese2Observer = mock(Observer.class);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            all.observeForever(allObserver);
            cheese2.observeForever(cheese2Observer);
        });

        // Insert an item
        mDatabase.cheese().insertAll(Arrays.asList(new Cheese(1, "first", false)));
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                new Cheese(1, "first", false))));
        verify(cheese2Observer, never()).onChanged(any(Cheese.class));
        assertThat(mDatabase.cheese().count(), is(1));

        // Insert another item
        mDatabase.cheese().insertAll(Arrays.asList(new Cheese(2, "second", true)));
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                new Cheese(1, "first", false),
                new Cheese(2, "second", true))));
        verify(cheese2Observer, db()).onChanged(eq(new Cheese(2, "second", true)));
        assertThat(mDatabase.cheese().count(), is(2));

        // Modify the second item
        mDatabase.cheese().update(new Cheese(2, "second", false));
        verify(allObserver, db()).onChanged(argThat(consistsOf(
                new Cheese(1, "first", false),
                new Cheese(2, "second", false))));
        verify(cheese2Observer, db()).onChanged(eq(new Cheese(2, "second", false)));
        assertThat(mDatabase.cheese().count(), is(2));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            all.removeObserver(allObserver);
            cheese2.removeObserver(cheese2Observer);
        });
    }

    private static VerificationMode db() {
        return timeout(3000).atLeastOnce();
    }

    @SuppressWarnings("unchecked")
    private static <T> ArgumentMatcher<List<T>> consistsOf(T... items) {
        return list -> {
            if (items.length != list.size()) {
                return false;
            }
            for (T item : items) {
                if (!list.contains(item)) {
                    return false;
                }
            }
            return true;
        };
    }

}
