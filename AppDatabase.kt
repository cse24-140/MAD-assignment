package com.example.studentaccom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Accommodation::class], version = 3, exportSchema = false) // Changed version to 3
abstract class AppDatabase : RoomDatabase() {
    abstract fun accommodationDao(): AccommodationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_accom_v3" // Changed name to force a fresh start
                )
                    .fallbackToDestructiveMigration() // This is the crash-preventer
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
