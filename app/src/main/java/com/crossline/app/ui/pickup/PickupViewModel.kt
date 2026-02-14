package com.crossline.app.ui.pickup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.entity.PickupRecord
import com.crossline.app.data.repository.PickupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupViewModel @Inject constructor(
    private val repository: PickupRepository
) : ViewModel() {

    val records: StateFlow<List<PickupRecord>> = repository.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _runCount = MutableStateFlow(0)
    val runCount: StateFlow<Int> = _runCount.asStateFlow()

    private val _missCount = MutableStateFlow(0)
    val missCount: StateFlow<Int> = _missCount.asStateFlow()

    init { refreshCounts() }

    private fun refreshCounts() {
        viewModelScope.launch {
            _runCount.value = repository.getCountByType("RUN")
            _missCount.value = repository.getCountByType("MISS")
        }
    }

    fun addRecord(classification: String, photoPath: String?, location: String?, memo: String?, tags: String?) {
        viewModelScope.launch {
            repository.insertRecord(
                PickupRecord(
                    classification = classification,
                    encryptedPhotoPath = photoPath,
                    location = location,
                    memo = memo,
                    tags = tags
                )
            )
            refreshCounts()
        }
    }

    fun deleteRecord(record: PickupRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            refreshCounts()
        }
    }
}
