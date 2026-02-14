package com.crossline.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Pickup session record - stored in SEPARATE encrypted database.
 * NEVER sent to any AI API. AES-256 local encryption only.
 */
@Entity(tableName = "pickup_records")
data class PickupRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val classification: String, // "RUN" or "MISS"
    val encryptedPhotoPath: String? = null,
    val location: String? = null,
    val memo: String? = null,
    val tags: String? = null
)
