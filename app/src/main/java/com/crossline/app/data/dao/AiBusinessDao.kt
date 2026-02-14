package com.crossline.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.crossline.app.data.entity.AiProject
import com.crossline.app.data.entity.AiProjectTab
import kotlinx.coroutines.flow.Flow

@Dao
interface AiBusinessDao {
    // Projects
    @Insert
    suspend fun insertProject(project: AiProject): Long

    @Update
    suspend fun updateProject(project: AiProject)

    @Delete
    suspend fun deleteProject(project: AiProject)

    @Query("SELECT * FROM ai_projects WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveProjects(): Flow<List<AiProject>>

    @Query("SELECT * FROM ai_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<AiProject>>

    // Tabs
    @Insert
    suspend fun insertTab(tab: AiProjectTab): Long

    @Update
    suspend fun updateTab(tab: AiProjectTab)

    @Query("SELECT * FROM ai_project_tabs WHERE projectId = :projectId ORDER BY tabType")
    fun getTabsForProject(projectId: Long): Flow<List<AiProjectTab>>
}
