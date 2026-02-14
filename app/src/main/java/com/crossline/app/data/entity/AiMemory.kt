package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * AI Assistant long-term memory: Daily conversation summary.
 * Loaded by assistant next day for continuity.
 */
@Entity(tableName = "ai_memories")
data class AiMemory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val summaryJson: String, // structured JSON summary of the day
    val sessionContext: String? = null, // which sessions were active
    val createdAt: Long = System.currentTimeMillis()
)
