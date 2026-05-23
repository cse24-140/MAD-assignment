package com.example.studentaccom

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccommodationDao {
    @Query("SELECT * FROM accommodations")
    fun getAll(): Flow<List<Accommodation>>

    @Query("SELECT * FROM accommodations")
    suspend fun getAllSync(): List<Accommodation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(accommodation: Accommodation)

    @Delete
    suspend fun delete(accommodation: Accommodation)
}