package com.crossline.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.crossline.app.data.dao.AiBusinessDao
import com.crossline.app.data.dao.AiMemoryDao
import com.crossline.app.data.dao.DietDao
import com.crossline.app.data.dao.InvestmentDao
import com.crossline.app.data.dao.StudyDao
import com.crossline.app.data.entity.AiMemory
import com.crossline.app.data.entity.AiProject
import com.crossline.app.data.entity.AiProjectTab
import com.crossline.app.data.entity.BodyCheck
import com.crossline.app.data.entity.DietEntry
import com.crossline.app.data.entity.InvestmentLog
import com.crossline.app.data.entity.StudyArchive
import com.crossline.app.data.entity.StudyCheckItem

/**
 * Main Room database for CrossLine.
 * Contains all session data EXCEPT Pickup (which uses PickupDatabase).
 * AI assistant can access all data in this database.
 */
@Database(
    entities = [
        DietEntry::class,
        BodyCheck::class,
        StudyCheckItem::class,
        StudyArchive::class,
        AiProject::class,
        AiProjectTab::class,
        InvestmentLog::class,
        AiMemory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CrossLineDatabase : RoomDatabase() {
    abstract fun dietDao(): DietDao
    abstract fun studyDao(): StudyDao
    abstract fun aiBusinessDao(): AiBusinessDao
    abstract fun investmentDao(): InvestmentDao
    abstract fun aiMemoryDao(): AiMemoryDao
}
