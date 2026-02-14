package com.crossline.app.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crossline.app.data.repository.AiAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatBubble(
    val role: String, // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    private val repository: AiAssistantRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatBubble>>(emptyList())
    val messages: StateFlow<List<ChatBubble>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isConfigured = MutableStateFlow(repository.isConfigured())
    val isConfigured: StateFlow<Boolean> = _isConfigured.asStateFlow()

    private var memoryContext: String? = null

    init {
        loadMemory()
    }

    private fun loadMemory() {
        viewModelScope.launch {
            val todayMemory = repository.loadTodayMemory()
            val recentMemories = repository.loadRecentMemories()
            memoryContext = buildString {
                if (todayMemory != null) {
                    append("오늘 기억: $todayMemory\n")
                }
                recentMemories.filter { it.summaryJson != todayMemory }.take(3).forEach {
                    append("[${it.date}] ${it.summaryJson}\n")
                }
            }.ifBlank { null }
        }
    }

    fun setApiKey(key: String) {
        repository.setApiKey(key)
        _isConfigured.value = repository.isConfigured()
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return

        val userBubble = ChatBubble(role = "user", content = text)
        _messages.value = _messages.value + userBubble
        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.chat(text, memoryContext)
            result.onSuccess { reply ->
                _messages.value = _messages.value + ChatBubble(role = "assistant", content = reply)
            }.onFailure { error ->
                _messages.value = _messages.value + ChatBubble(
                    role = "assistant",
                    content = "오류: ${error.message ?: "알 수 없는 오류"}"
                )
            }
            _isLoading.value = false
        }
    }

    fun saveDailySummary() {
        viewModelScope.launch {
            val result = repository.generateDailySummary()
            result.onSuccess { summary ->
                repository.saveDailySummary(summary, null)
            }
        }
    }

    fun clearChat() {
        _messages.value = emptyList()
        repository.clearConversation()
    }
}
