package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MoneyTrack - Study: Archive of external AI study logs (copy-paste storage).
 */
@Entity(tableName = "study_archives")
data class StudyArchive(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val content: String, // pasted study log
    val source: String? = null, // e.g., "ChatGPT", "Claude"
    val tags: String? = null // comma-separated tags
)
