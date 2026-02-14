package com.crossline.app.data.repository

import com.crossline.app.data.dao.PickupDao
import com.crossline.app.data.entity.PickupRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for Pickup session - operates on ENCRYPTED database only.
 * Data from this repository must NEVER be sent to any AI API.
 */
@Singleton
class PickupRepository @Inject constructor(
    private val pickupDao: PickupDao
) {
    fun getAllRecords(): Flow<List<PickupRecord>> = pickupDao.getAllRecords()
    fun getRecordsByType(type: String): Flow<List<PickupRecord>> = pickupDao.getRecordsByType(type)
    suspend fun getCountByType(type: String): Int = pickupDao.getCountByType(type)
    suspend fun insertRecord(record: PickupRecord): Long = pickupDao.insertRecord(record)
    suspend fun updateRecord(record: PickupRecord) = pickupDao.updateRecord(record)
    suspend fun deleteRecord(record: PickupRecord) = pickupDao.deleteRecord(record)
}
