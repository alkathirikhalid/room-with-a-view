package com.alkathirikhalid.androidroomwithaview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        // Singleton Instance
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            // Check instance is null create else return existing instance
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context, scope)
                INSTANCE = instance
                // Return synchronized instance
                instance
            }
        }

        // Build Data Base
        private fun buildDatabase(context: Context, scope: CoroutineScope) = Room.databaseBuilder(
            context.applicationContext,
            WordRoomDatabase::class.java,
            "word_database"
        ).addCallback(WordDatabaseCallback(scope)).build()
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)
        }
    }

}