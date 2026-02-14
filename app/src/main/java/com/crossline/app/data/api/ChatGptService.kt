package com.crossline.app.data.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

data class ChatMessage(
    val role: String, // "system", "user", "assistant"
    val content: String
)

data class GptRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<ChatMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 1024,
    val temperature: Float = 0.7f
)

data class GptResponse(
    val choices: List<Choice>?
) {
    data class Choice(val message: ChatMessage?)
}

/**
 * ChatGPT API client.
 * API key should be set via setApiKey() before use.
 * Pickup data must NEVER pass through this service.
 */
@Singleton
class ChatGptService @Inject constructor() {

    private var apiKey: String = ""
    private val gson = Gson()

    fun setApiKey(key: String) { apiKey = key }
    fun isConfigured(): Boolean = apiKey.isNotBlank()

    suspend fun sendMessage(
        messages: List<ChatMessage>
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("API 키가 설정되지 않았습니다"))
        }

        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer $apiKey")
                doOutput = true
                connectTimeout = 30000
                readTimeout = 60000
            }

            val request = GptRequest(messages = messages)
            val body = gson.toJson(request)

            OutputStreamWriter(conn.outputStream).use { it.write(body) }

            if (conn.responseCode == 200) {
                val response = BufferedReader(InputStreamReader(conn.inputStream)).use { it.readText() }
                val gptResponse = gson.fromJson(response, GptResponse::class.java)
                val content = gptResponse.choices?.firstOrNull()?.message?.content
                    ?: "응답을 받지 못했습니다."
                Result.success(content)
            } else {
                val error = BufferedReader(InputStreamReader(conn.errorStream)).use { it.readText() }
                Result.failure(Exception("API 오류 (${conn.responseCode}): $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
