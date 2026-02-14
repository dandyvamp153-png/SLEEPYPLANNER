package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Pivot Camera body-check record with InBody data and condition score.
 */
@Entity(tableName = "body_checks")
data class BodyCheck(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val photoUri: String? = null,
    val conditionScore: Int = 3, // 1-5 scale
    val weight: Float? = null,
    val muscleMass: Float? = null,
    val bodyFatPercent: Float? = null,
    val memo: String? = null
)
