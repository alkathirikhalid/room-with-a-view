package com.alkathirikhalid.androidroomwithaview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        // Singleton Instance
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            // Check instance is null create else return existing instance
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                // Return synchronized instance
                instance
            }
        }

        // Build Data Base
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WordRoomDatabase::class.java,
            "word_database"
        ).build()
    }
}