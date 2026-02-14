package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crossline.app.data.entity.AiMemory
import kotlinx.coroutines.flow.Flow

@Dao
interface AiMemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: AiMemory): Long

    @Query("SELECT * FROM ai_memories WHERE date = :date LIMIT 1")
    suspend fun getMemoryForDate(date: String): AiMemory?

    @Query("SELECT * FROM ai_memories ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentMemories(limit: Int = 7): List<AiMemory>

    @Query("SELECT * FROM ai_memories ORDER BY date DESC")
    fun getAllMemories(): Flow<List<AiMemory>>
}
