package com.alkathirikhalid.androidroomwithaview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    // CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE) // C
    suspend fun insert(word: Word)

    @Query("SELECT * FROM word_table ORDER BY word ASC") // RU
    // Flow produces values one at a time (instead of all at once)
    // that can generate values from async operations
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Query("DELETE FROM word_table") // D
    suspend fun deleteAll()
}