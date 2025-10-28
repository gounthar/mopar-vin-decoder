package com.moparvindecoder.ui.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moparvindecoder.domain.model.VinInfo
import com.moparvindecoder.domain.usecase.DecodeVinUseCase
import com.moparvindecoder.data.repository.VinHistoryRepository
import com.moparvindecoder.data.local.VinHistoryEntity
import com.moparvindecoder.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VinResultsViewModel(
    private val decodeVin: DecodeVinUseCase,
    private val historyRepo: VinHistoryRepository
) : ViewModel() {

    private val _vinInfo = MutableLiveData<Result<VinInfo>>()
    val vinInfo: LiveData<Result<VinInfo>> get() = _vinInfo

    fun decode(vin: String) {
        viewModelScope.launch {
            val res = decodeVin(vin)
            _vinInfo.value = res
            if (res is Result.Success) {
                val info = res.data
                try {
                    // Explicitly use IO dispatcher for database operations
                    withContext(Dispatchers.IO) {
                        historyRepo.upsert(
                            VinHistoryEntity(
                                vin = info.vin,
                                year = info.year,
                                make = info.make,
                                model = info.model
                            )
                        )
                    }
                } catch (e: Exception) {
                    // Non-fatal error: log but don't crash the app
                    Timber.e(e, "Failed to save VIN to history")
                }
            }
        }
    }
}

class VinResultsViewModelFactory(
    private val decodeVin: DecodeVinUseCase,
    private val historyRepo: VinHistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VinResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VinResultsViewModel(decodeVin, historyRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
