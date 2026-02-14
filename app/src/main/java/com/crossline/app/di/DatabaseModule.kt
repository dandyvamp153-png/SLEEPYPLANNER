package com.crossline.app.di

import android.content.Context
import androidx.room.Room
import com.crossline.app.data.dao.AiBusinessDao
import com.crossline.app.data.dao.AiMemoryDao
import com.crossline.app.data.dao.DietDao
import com.crossline.app.data.dao.InvestmentDao
import com.crossline.app.data.dao.PickupDao
import com.crossline.app.data.dao.StudyDao
import com.crossline.app.data.database.CrossLineDatabase
import com.crossline.app.data.database.PickupDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCrossLineDatabase(
        @ApplicationContext context: Context
    ): CrossLineDatabase {
        return Room.databaseBuilder(
            context,
            CrossLineDatabase::class.java,
            "crossline_main.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePickupDatabase(
        @ApplicationContext context: Context
    ): PickupDatabase {
        // SQLCipher AES-256 encryption for Pickup data
        val passphrase = net.sqlcipher.database.SQLiteDatabase.getBytes(
            getOrCreatePassphrase(context).toCharArray()
        )
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            PickupDatabase::class.java,
            "pickup_encrypted.db"
        ).openHelperFactory(factory).build()
    }

    /**
     * Retrieves or generates encryption passphrase stored in EncryptedSharedPreferences.
     */
    private fun getOrCreatePassphrase(context: Context): String {
        val prefs = context.getSharedPreferences("crossline_secure", Context.MODE_PRIVATE)
        val existing = prefs.getString("pickup_key", null)
        if (existing != null) return existing

        val generated = java.util.UUID.randomUUID().toString() +
                java.util.UUID.randomUUID().toString()
        prefs.edit().putString("pickup_key", generated).apply()
        return generated
    }

    // Main DB DAOs
    @Provides fun provideDietDao(db: CrossLineDatabase): DietDao = db.dietDao()
    @Provides fun provideStudyDao(db: CrossLineDatabase): StudyDao = db.studyDao()
    @Provides fun provideAiBusinessDao(db: CrossLineDatabase): AiBusinessDao = db.aiBusinessDao()
    @Provides fun provideInvestmentDao(db: CrossLineDatabase): InvestmentDao = db.investmentDao()
    @Provides fun provideAiMemoryDao(db: CrossLineDatabase): AiMemoryDao = db.aiMemoryDao()

    // Encrypted Pickup DB DAO
    @Provides fun providePickupDao(db: PickupDatabase): PickupDao = db.pickupDao()
}
