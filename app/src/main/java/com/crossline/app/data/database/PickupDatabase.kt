package com.crossline.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crossline.app.data.dao.PickupDao
import com.crossline.app.data.entity.PickupRecord

/**
 * Encrypted Room database for Pickup session ONLY.
 * Uses SQLCipher for AES-256 encryption.
 * Data in this database must NEVER be sent to any AI API.
 */
@Database(
    entities = [PickupRecord::class],
    version = 1,
    exportSchema = false
)
abstract class PickupDatabase : RoomDatabase() {
    abstract fun pickupDao(): PickupDao
}
