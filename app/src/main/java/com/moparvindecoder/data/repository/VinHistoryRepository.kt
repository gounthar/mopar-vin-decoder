package com.moparvindecoder.data.repository

import com.moparvindecoder.data.local.VinHistoryDao
import com.moparvindecoder.data.local.VinHistoryEntity
import kotlinx.coroutines.flow.Flow

interface VinHistoryRepository {
    fun observeHistory(): Flow<List<VinHistoryEntity>>
    suspend fun upsert(entity: VinHistoryEntity)
    suspend fun clear()
}

class VinHistoryRepositoryImpl(
    private val dao: VinHistoryDao
) : VinHistoryRepository {
    override fun observeHistory(): Flow<List<VinHistoryEntity>> = dao.observeHistory()
    override suspend fun upsert(entity: VinHistoryEntity) = dao.upsert(entity)
    override suspend fun clear() = dao.clear()
}
