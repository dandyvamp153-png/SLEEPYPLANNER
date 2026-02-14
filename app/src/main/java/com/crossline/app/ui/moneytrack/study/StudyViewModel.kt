package com.crossline.app.ui.moneytrack.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.entity.StudyArchive
import com.crossline.app.data.entity.StudyCheckItem
import com.crossline.app.data.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val repository: StudyRepository
) : ViewModel() {

    val checkItems: StateFlow<List<StudyCheckItem>> = repository.getAllCheckItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archives: StateFlow<List<StudyArchive>> = repository.getAllArchives()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTab = MutableStateFlow(0) // 0=체크리스트, 1=아카이브
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(index: Int) { _selectedTab.value = index }

    fun addCheckItem(examType: String, year: Int, roundNumber: Int) {
        viewModelScope.launch {
            repository.insertCheckItem(
                StudyCheckItem(examType = examType, year = year, roundNumber = roundNumber)
            )
        }
    }

    fun toggleCheckItem(item: StudyCheckItem) {
        viewModelScope.launch {
            repository.updateCheckItem(
                item.copy(
                    isCompleted = !item.isCompleted,
                    completedAt = if (!item.isCompleted) System.currentTimeMillis() else null
                )
            )
        }
    }

    fun deleteCheckItem(item: StudyCheckItem) {
        viewModelScope.launch { repository.deleteCheckItem(item) }
    }

    fun addArchive(title: String, content: String, source: String?) {
        viewModelScope.launch {
            repository.insertArchive(
                StudyArchive(title = title, content = content, source = source)
            )
        }
    }

    fun deleteArchive(archive: StudyArchive) {
        viewModelScope.launch { repository.deleteArchive(archive) }
    }
}
