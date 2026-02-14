package com.crossline.app.data.repository

import com.crossline.app.data.dao.DietDao
import com.crossline.app.data.entity.BodyCheck
import com.crossline.app.data.entity.DietEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DietRepository @Inject constructor(
    private val dietDao: DietDao
) {
    // Meal entries
    fun getAllEntries(): Flow<List<DietEntry>> = dietDao.getAllEntries()

    fun getEntriesBetween(start: Long, end: Long): Flow<List<DietEntry>> =
        dietDao.getEntriesBetween(start, end)

    suspend fun insertEntry(entry: DietEntry): Long = dietDao.insertEntry(entry)
    suspend fun updateEntry(entry: DietEntry) = dietDao.updateEntry(entry)
    suspend fun deleteEntry(entry: DietEntry) = dietDao.deleteEntry(entry)

    // Body checks
    fun getAllBodyChecks(): Flow<List<BodyCheck>> = dietDao.getAllBodyChecks()
    suspend fun getLatestBodyCheck(): BodyCheck? = dietDao.getLatestBodyCheck()
    suspend fun insertBodyCheck(check: BodyCheck): Long = dietDao.insertBodyCheck(check)
    suspend fun updateBodyCheck(check: BodyCheck) = dietDao.updateBodyCheck(check)
    suspend fun deleteBodyCheck(check: BodyCheck) = dietDao.deleteBodyCheck(check)
}
