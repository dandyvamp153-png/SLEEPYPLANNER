package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MoneyTrack - Study: Exam rotation checklist item for Industrial Safety Engineer.
 */
@Entity(tableName = "study_check_items")
data class StudyCheckItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examType: String, // "WRITTEN" or "PRACTICAL"
    val year: Int, // exam year
    val roundNumber: Int, // rotation count (회독)
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val memo: String? = null
)
