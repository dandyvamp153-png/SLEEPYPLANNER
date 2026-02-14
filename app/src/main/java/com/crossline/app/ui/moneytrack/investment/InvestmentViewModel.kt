package com.crossline.app.ui.moneytrack.investment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.entity.InvestmentLog
import com.crossline.app.data.repository.InvestmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvestmentViewModel @Inject constructor(
    private val repository: InvestmentRepository
) : ViewModel() {

    val logs: StateFlow<List<InvestmentLog>> = repository.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addLog(title: String, content: String, version: String?, category: String) {
        viewModelScope.launch {
            repository.insertLog(
                InvestmentLog(
                    title = title,
                    content = content,
                    version = version,
                    category = category
                )
            )
        }
    }

    fun deleteLog(log: InvestmentLog) {
        viewModelScope.launch { repository.deleteLog(log) }
    }
}
