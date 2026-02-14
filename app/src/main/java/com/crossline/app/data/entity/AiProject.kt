package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MoneyTrack - AI Business: Project entry (e.g., MeongnyangSignal).
 */
@Entity(tableName = "ai_projects")
data class AiProject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
