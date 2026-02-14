package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * MoneyTrack - Investment: Trading automation development log.
 */
@Entity(tableName = "investment_logs")
data class InvestmentLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val content: String,
    val version: String? = null, // program version if applicable
    val category: String? = null // "DEV_LOG" or "UPDATE"
)
