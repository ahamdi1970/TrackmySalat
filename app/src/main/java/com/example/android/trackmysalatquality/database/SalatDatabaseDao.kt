/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysalatquality.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SalatDatabaseDao {

    @Insert
    suspend fun insert(salat: SalatData)

    @Update
    suspend fun update(salat: SalatData)

    @Query("SELECT * from daily_salat_table WHERE salatID = :key")
    suspend fun get(key: Long): SalatData?

    @Query("DELETE FROM daily_salat_table")
    suspend fun clear()

    @Query("SELECT * FROM daily_salat_table ORDER BY salatID DESC LIMIT 1")
    suspend fun getSalat(): SalatData?

    @Query("SELECT * FROM daily_salat_table ORDER BY salatID DESC")
    fun getAllSalates(): LiveData<List<SalatData>>
}
