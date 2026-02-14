package com.crossline.app.data.repository

import com.crossline.app.data.dao.StudyDao
import com.crossline.app.data.entity.StudyArchive
import com.crossline.app.data.entity.StudyCheckItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyRepository @Inject constructor(
    private val studyDao: StudyDao
) {
    fun getAllCheckItems(): Flow<List<StudyCheckItem>> = studyDao.getAllCheckItems()
    fun getCheckItemsByType(type: String): Flow<List<StudyCheckItem>> = studyDao.getCheckItemsByType(type)
    suspend fun insertCheckItem(item: StudyCheckItem): Long = studyDao.insertCheckItem(item)
    suspend fun updateCheckItem(item: StudyCheckItem) = studyDao.updateCheckItem(item)
    suspend fun deleteCheckItem(item: StudyCheckItem) = studyDao.deleteCheckItem(item)

    fun getAllArchives(): Flow<List<StudyArchive>> = studyDao.getAllArchives()
    suspend fun insertArchive(archive: StudyArchive): Long = studyDao.insertArchive(archive)
    suspend fun updateArchive(archive: StudyArchive) = studyDao.updateArchive(archive)
    suspend fun deleteArchive(archive: StudyArchive) = studyDao.deleteArchive(archive)
}
