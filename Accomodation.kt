package com.example.studentaccom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accommodations")
data class Accommodation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val price: Double,
    val type: String,
    val amenities: String,
    val deposit: Double,      // Add this line
    val providerPhone: String, // Add this line
    val imageUrl: String
)