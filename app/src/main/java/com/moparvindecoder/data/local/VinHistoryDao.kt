package com.moparvindecoder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VinHistoryDao {

    @Query("SELECT * FROM vin_history ORDER BY timestamp DESC")
    fun observeHistory(): Flow<List<VinHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: VinHistoryEntity)

    @Query("DELETE FROM vin_history")
    suspend fun clear()

    @Query("SELECT * FROM vin_history WHERE vin = :vin LIMIT 1")
    suspend fun getByVin(vin: String): VinHistoryEntity?
}
