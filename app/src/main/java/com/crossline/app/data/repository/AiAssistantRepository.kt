package com.crossline.app.data.repository

import com.crossline.app.data.api.ChatGptService
import com.crossline.app.data.api.ChatMessage
import com.crossline.app.data.dao.AiMemoryDao
import com.crossline.app.data.entity.AiMemory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for AI Assistant - manages conversation, API calls, and long-term memory.
 * NEVER includes Pickup session data in any context sent to the API.
 */
@Singleton
class AiAssistantRepository @Inject constructor(
    private val chatGptService: ChatGptService,
    private val aiMemoryDao: AiMemoryDao
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val conversationHistory = mutableListOf<ChatMessage>()

    fun isConfigured(): Boolean = chatGptService.isConfigured()
    fun setApiKey(key: String) = chatGptService.setApiKey(key)

    suspend fun loadTodayMemory(): String? {
        val today = dateFormat.format(Date())
        return aiMemoryDao.getMemoryForDate(today)?.summaryJson
    }

    suspend fun loadRecentMemories(days: Int = 7): List<AiMemory> {
        return aiMemoryDao.getRecentMemories(days)
    }

    fun getSystemPrompt(memoryContext: String?): ChatMessage {
        val base = """당신은 CrossLine 앱의 AI 비서입니다.
역할: 데이터 구조화, 일일 브리핑, 전략 조언
관리 영역: 다이어트(식단/운동), 머니트랙(학습/AI창업/투자)
절대 규칙: 픽업(Pickup) 세션에 대해서는 어떤 데이터도 언급하거나 접근하지 않습니다.
한국어로 응답하세요. 간결하고 실용적으로 답변하세요."""

        val withMemory = if (memoryContext != null) {
            "$base\n\n[이전 기억]\n$memoryContext"
        } else base

        return ChatMessage(role = "system", content = withMemory)
    }

    suspend fun chat(userMessage: String, memoryContext: String?): Result<String> {
        if (conversationHistory.isEmpty()) {
            conversationHistory.add(getSystemPrompt(memoryContext))
        }

        conversationHistory.add(ChatMessage(role = "user", content = userMessage))

        val result = chatGptService.sendMessage(conversationHistory)

        result.onSuccess { reply ->
            conversationHistory.add(ChatMessage(role = "assistant", content = reply))
        }

        return result
    }

    suspend fun saveDailySummary(summaryJson: String, sessionContext: String?) {
        val today = dateFormat.format(Date())
        aiMemoryDao.insertMemory(
            AiMemory(
                date = today,
                summaryJson = summaryJson,
                sessionContext = sessionContext
            )
        )
    }

    suspend fun generateDailySummary(): Result<String> {
        if (conversationHistory.size <= 1) {
            return Result.success("{}")
        }

        val summaryPrompt = conversationHistory.toMutableList().apply {
            add(ChatMessage(
                role = "user",
                content = "오늘 대화를 JSON 형식으로 요약해줘. 키: diet_summary, study_summary, business_summary, investment_summary, key_decisions, tomorrow_priorities. 해당 없는 항목은 null로."
            ))
        }

        return chatGptService.sendMessage(summaryPrompt)
    }

    fun clearConversation() {
        conversationHistory.clear()
    }
}
