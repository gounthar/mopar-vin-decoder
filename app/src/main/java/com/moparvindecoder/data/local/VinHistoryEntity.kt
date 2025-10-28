package com.moparvindecoder.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vin_history",
    indices = [Index(value = ["vin"], unique = true)]
)
data class VinHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "vin", collate = ColumnInfo.NOCASE)
    val vin: String,
    @ColumnInfo(name = "year")
    val year: String? = null,
    @ColumnInfo(name = "make")
    val make: String? = null,
    @ColumnInfo(name = "model")
    val model: String? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
