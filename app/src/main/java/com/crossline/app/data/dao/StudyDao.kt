package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crossline.app.data.entity.StudyArchive
import com.crossline.app.data.entity.StudyCheckItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    // Checklist
    @Insert
    suspend fun insertCheckItem(item: StudyCheckItem): Long

    @Update
    suspend fun updateCheckItem(item: StudyCheckItem)

    @Delete
    suspend fun deleteCheckItem(item: StudyCheckItem)

    @Query("SELECT * FROM study_check_items ORDER BY examType, year, roundNumber")
    fun getAllCheckItems(): Flow<List<StudyCheckItem>>

    @Query("SELECT * FROM study_check_items WHERE examType = :type ORDER BY year, roundNumber")
    fun getCheckItemsByType(type: String): Flow<List<StudyCheckItem>>

    // Archive
    @Insert
    suspend fun insertArchive(archive: StudyArchive): Long

    @Update
    suspend fun updateArchive(archive: StudyArchive)

    @Delete
    suspend fun deleteArchive(archive: StudyArchive)

    @Query("SELECT * FROM study_archives ORDER BY timestamp DESC")
    fun getAllArchives(): Flow<List<StudyArchive>>
}
