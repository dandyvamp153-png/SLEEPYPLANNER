package com.crossline.app.ui.moneytrack.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.entity.AiProject
import com.crossline.app.data.entity.AiProjectTab
import com.crossline.app.data.repository.AiBusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiBusinessViewModel @Inject constructor(
    private val repository: AiBusinessRepository
) : ViewModel() {

    val projects: StateFlow<List<AiProject>> = repository.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProjectId = MutableStateFlow<Long?>(null)
    val selectedProjectId: StateFlow<Long?> = _selectedProjectId.asStateFlow()

    val tabs: StateFlow<List<AiProjectTab>> = _selectedProjectId
        .flatMapLatest { id ->
            if (id != null) repository.getTabsForProject(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectProject(id: Long) { _selectedProjectId.value = id }

    fun addProject(name: String, description: String?) {
        viewModelScope.launch {
            val projectId = repository.insertProject(
                AiProject(name = name, description = description)
            )
            // Auto-create 7 management tabs
            AiBusinessRepository.TAB_TYPES.forEach { tabType ->
                repository.insertTab(
                    AiProjectTab(projectId = projectId, tabType = tabType)
                )
            }
            _selectedProjectId.value = projectId
        }
    }

    fun deleteProject(project: AiProject) {
        viewModelScope.launch {
            repository.deleteProject(project)
            if (_selectedProjectId.value == project.id) {
                _selectedProjectId.value = null
            }
        }
    }

    fun updateTabContent(tab: AiProjectTab, content: String) {
        viewModelScope.launch {
            repository.updateTab(tab.copy(content = content, updatedAt = System.currentTimeMillis()))
        }
    }
}
