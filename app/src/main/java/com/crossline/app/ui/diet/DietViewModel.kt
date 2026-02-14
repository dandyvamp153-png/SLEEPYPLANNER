package com.crossline.app.ui.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.entity.BodyCheck
import com.crossline.app.data.entity.DietEntry
import com.crossline.app.data.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    val entries: StateFlow<List<DietEntry>> = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bodyChecks: StateFlow<List<BodyCheck>> = repository.getAllBodyChecks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _latestBodyCheck = MutableStateFlow<BodyCheck?>(null)
    val latestBodyCheck: StateFlow<BodyCheck?> = _latestBodyCheck.asStateFlow()

    init {
        viewModelScope.launch {
            _latestBodyCheck.value = repository.getLatestBodyCheck()
        }
    }

    fun addEntry(status: String, photoUri: String?, memo: String?) {
        viewModelScope.launch {
            repository.insertEntry(
                DietEntry(status = status, photoUri = photoUri, memo = memo)
            )
        }
    }

    fun deleteEntry(entry: DietEntry) {
        viewModelScope.launch { repository.deleteEntry(entry) }
    }

    fun addBodyCheck(
        photoUri: String?,
        conditionScore: Int,
        weight: Float?,
        muscleMass: Float?,
        bodyFatPercent: Float?,
        memo: String?
    ) {
        viewModelScope.launch {
            val check = BodyCheck(
                photoUri = photoUri,
                conditionScore = conditionScore,
                weight = weight,
                muscleMass = muscleMass,
                bodyFatPercent = bodyFatPercent,
                memo = memo
            )
            repository.insertBodyCheck(check)
            _latestBodyCheck.value = check
        }
    }

    fun deleteBodyCheck(check: BodyCheck) {
        viewModelScope.launch { repository.deleteBodyCheck(check) }
    }
}
