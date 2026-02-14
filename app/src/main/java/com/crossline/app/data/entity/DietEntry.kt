package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Diet session: Good/Bad meal log with optional GPT analysis.
 */
@Entity(tableName = "diet_entries")
data class DietEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String, // "GOOD" or "BAD"
    val photoUri: String? = null,
    val calorieEstimate: Int? = null,
    val nutritionJson: String? = null, // GPT analysis result as JSON
    val memo: String? = null
)
