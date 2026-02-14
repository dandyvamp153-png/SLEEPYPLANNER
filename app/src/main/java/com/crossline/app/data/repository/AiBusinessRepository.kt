package com.crossline.app.data.repository

import com.crossline.app.data.dao.AiBusinessDao
import com.crossline.app.data.entity.AiProject
import com.crossline.app.data.entity.AiProjectTab
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiBusinessRepository @Inject constructor(
    private val aiBusinessDao: AiBusinessDao
) {
    fun getActiveProjects(): Flow<List<AiProject>> = aiBusinessDao.getActiveProjects()
    fun getAllProjects(): Flow<List<AiProject>> = aiBusinessDao.getAllProjects()
    suspend fun insertProject(project: AiProject): Long = aiBusinessDao.insertProject(project)
    suspend fun updateProject(project: AiProject) = aiBusinessDao.updateProject(project)
    suspend fun deleteProject(project: AiProject) = aiBusinessDao.deleteProject(project)

    fun getTabsForProject(projectId: Long): Flow<List<AiProjectTab>> =
        aiBusinessDao.getTabsForProject(projectId)
    suspend fun insertTab(tab: AiProjectTab): Long = aiBusinessDao.insertTab(tab)
    suspend fun updateTab(tab: AiProjectTab) = aiBusinessDao.updateTab(tab)

    companion object {
        val TAB_TYPES = listOf("MVP", "OFFICE", "GRANTS", "MARKETING", "BM", "NETWORK", "LEGAL")
        val TAB_LABELS = mapOf(
            "MVP" to "MVP 제작",
            "OFFICE" to "입주공간",
            "GRANTS" to "지원사업",
            "MARKETING" to "마케팅/채널",
            "BM" to "비즈니스 모델",
            "NETWORK" to "네트워크/멘토",
            "LEGAL" to "법무/노무"
        )
    }
}
