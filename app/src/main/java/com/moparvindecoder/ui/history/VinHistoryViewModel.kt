package com.moparvindecoder.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.moparvindecoder.data.local.AppDatabase
import com.moparvindecoder.data.local.VinHistoryEntity
import com.moparvindecoder.data.repository.VinHistoryRepository
import com.moparvindecoder.data.repository.VinHistoryRepositoryImpl

class VinHistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: VinHistoryRepository =
        VinHistoryRepositoryImpl(AppDatabase.get(app).vinHistoryDao())

    val history: LiveData<List<VinHistoryEntity>> = repository.observeHistory().asLiveData()

    suspend fun clear() = repository.clear()
}
