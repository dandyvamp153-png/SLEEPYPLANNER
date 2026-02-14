package com.crossline.app.data.repository

import com.crossline.app.data.dao.InvestmentDao
import com.crossline.app.data.entity.InvestmentLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestmentRepository @Inject constructor(
    private val investmentDao: InvestmentDao
) {
    fun getAllLogs(): Flow<List<InvestmentLog>> = investmentDao.getAllLogs()
    fun getLogsByCategory(category: String): Flow<List<InvestmentLog>> =
        investmentDao.getLogsByCategory(category)
    suspend fun insertLog(log: InvestmentLog): Long = investmentDao.insertLog(log)
    suspend fun updateLog(log: InvestmentLog) = investmentDao.updateLog(log)
    suspend fun deleteLog(log: InvestmentLog) = investmentDao.deleteLog(log)
}
