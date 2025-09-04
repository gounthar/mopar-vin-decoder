package com.moparvindecoder.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.moparvindecoder.data.local.VinHistoryEntity
import com.moparvindecoder.data.repository.VinHistoryRepository

class VinHistoryViewModel(
    private val repository: VinHistoryRepository
) : ViewModel() {

    val history: LiveData<List<VinHistoryEntity>> = repository.observeHistory().asLiveData()

    suspend fun clear() = repository.clear()
}

class VinHistoryViewModelFactory(
    private val repository: VinHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VinHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VinHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
