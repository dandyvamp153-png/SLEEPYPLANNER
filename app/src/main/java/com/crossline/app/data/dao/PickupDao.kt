package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crossline.app.data.entity.PickupRecord
import kotlinx.coroutines.flow.Flow

/**
 * DAO for PickupRecord - operates on the ENCRYPTED database only.
 * Data from this DAO must NEVER be sent to any AI API.
 */
@Dao
interface PickupDao {
    @Insert
    suspend fun insertRecord(record: PickupRecord): Long

    @Update
    suspend fun updateRecord(record: PickupRecord)

    @Delete
    suspend fun deleteRecord(record: PickupRecord)

    @Query("SELECT * FROM pickup_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<PickupRecord>>

    @Query("SELECT * FROM pickup_records WHERE classification = :type ORDER BY timestamp DESC")
    fun getRecordsByType(type: String): Flow<List<PickupRecord>>

    @Query("SELECT COUNT(*) FROM pickup_records WHERE classification = :type")
    suspend fun getCountByType(type: String): Int
}
