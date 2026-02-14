package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crossline.app.data.entity.BodyCheck
import com.crossline.app.data.entity.DietEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert
    suspend fun insertEntry(entry: DietEntry): Long

    @Update
    suspend fun updateEntry(entry: DietEntry)

    @Delete
    suspend fun deleteEntry(entry: DietEntry)

    @Query("SELECT * FROM diet_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<DietEntry>>

    @Query("SELECT * FROM diet_entries WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getEntriesBetween(start: Long, end: Long): Flow<List<DietEntry>>

    @Insert
    suspend fun insertBodyCheck(check: BodyCheck): Long

    @Update
    suspend fun updateBodyCheck(check: BodyCheck)

    @Delete
    suspend fun deleteBodyCheck(check: BodyCheck)

    @Query("SELECT * FROM body_checks ORDER BY timestamp DESC")
    fun getAllBodyChecks(): Flow<List<BodyCheck>>

    @Query("SELECT * FROM body_checks ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestBodyCheck(): BodyCheck?
}
