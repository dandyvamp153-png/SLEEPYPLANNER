package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * MoneyTrack - AI Business: Tab entry under a project.
 * 7 tabs: MVP, Office Space, Government Grants, Marketing, BM, Network, Legal/Admin
 */
@Entity(
    tableName = "ai_project_tabs",
    foreignKeys = [
        ForeignKey(
            entity = AiProject::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("projectId")]
)
data class AiProjectTab(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val tabType: String, // MVP, OFFICE, GRANTS, MARKETING, BM, NETWORK, LEGAL
    val content: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
