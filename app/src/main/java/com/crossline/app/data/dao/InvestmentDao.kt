package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crossline.app.data.entity.InvestmentLog
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentDao {
    @Insert
    suspend fun insertLog(log: InvestmentLog): Long

    @Update
    suspend fun updateLog(log: InvestmentLog)

    @Delete
    suspend fun deleteLog(log: InvestmentLog)

    @Query("SELECT * FROM investment_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<InvestmentLog>>

    @Query("SELECT * FROM investment_logs WHERE category = :category ORDER BY timestamp DESC")
    fun getLogsByCategory(category: String): Flow<List<InvestmentLog>>
}
